const express = require('express');
const cors = require('cors');
const mysql = require('mysql2/promise');
const path = require('path');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, 'frontend')));

const dbConfig = {
  host: '127.0.0.1',
  port: Number(process.env.DB_PORT) || 3306,
  user: process.env.DB_USERNAME || 'root',
  password: process.env.DB_PASSWORD || '',
  database: 'student_evaluation',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
};

const pool = mysql.createPool(dbConfig);

async function getStudentByNumber(studentNumber) {
  const [rows] = await pool.query(
    'SELECT * FROM students WHERE student_number = ?',
    [studentNumber]
  );

  return rows[0] || null;
}

async function getStudentById(id) {
  const [rows] = await pool.query(
    'SELECT * FROM students WHERE id = ?',
    [id]
  );

  return rows[0] || null;
}

app.get('/api/health', async (req, res) => {
  try {
    await pool.query('SELECT 1');
    res.json({ ok: true, message: 'API is running and MySQL is reachable.' });
  } catch (error) {
    res.status(500).json({ ok: false, message: 'MySQL connection failed.', error: error.message });
  }
});

app.get('/api/students/login/:studentNumber', async (req, res) => {
  try {
    const student = await getStudentByNumber(req.params.studentNumber);

    if (!student) {
      return res.status(404).json({ message: 'Student not found.' });
    }

    res.json({
      id: student.id,
      studentNumber: student.student_number,
      name: student.name,
      course: student.course,
      yearLevel: student.year_level
    });
  } catch (error) {
    res.status(500).json({ message: 'Failed to login student.', error: error.message });
  }
});

app.get('/api/students/:id', async (req, res) => {
  try {
    const student = await getStudentById(req.params.id);

    if (!student) {
      return res.status(404).json({ message: 'Student not found.' });
    }

    res.json({
      id: student.id,
      studentNumber: student.student_number,
      name: student.name,
      course: student.course,
      yearLevel: student.year_level
    });
  } catch (error) {
    res.status(500).json({ message: 'Failed to fetch student.', error: error.message });
  }
});

app.post('/api/evaluations', async (req, res) => {
  try {
    const { studentId, instructor, subject, rating, comments, answers } = req.body;

    if (!studentId || !instructor || !subject || !answers || !Array.isArray(answers)) {
      return res.status(400).json({ message: 'Missing required evaluation fields.' });
    }

    const [existing] = await pool.query(
      'SELECT id FROM evaluations WHERE student_id = ? AND subject = ?',
      [studentId, subject]
    );

    if (existing.length > 0) {
      return res.status(400).json({ message: 'You have already evaluated this subject.' });
    }

    const [result] = await pool.query(
      `INSERT INTO evaluations (student_id, instructor, subject, rating, comments, submitted_at)
       VALUES (?, ?, ?, ?, ?, NOW())`,
      [studentId, instructor, subject, rating, comments || '']
    );

    const evaluationId = result.insertId;

    for (const answer of answers) {
      await pool.query(
        'INSERT INTO answers (evaluation_id, question_number, rating) VALUES (?, ?, ?)',
        [evaluationId, answer.questionNumber, answer.rating]
      );
    }

    const [rows] = await pool.query(
      'SELECT * FROM evaluations WHERE id = ?',
      [evaluationId]
    );

    const savedEvaluation = rows[0];

    res.status(201).json({
      id: savedEvaluation.id,
      studentId: savedEvaluation.student_id,
      instructor: savedEvaluation.instructor,
      subject: savedEvaluation.subject,
      rating: savedEvaluation.rating,
      comments: savedEvaluation.comments,
      submittedAt: savedEvaluation.submitted_at
    });
  } catch (error) {
    res.status(500).json({ message: 'Failed to submit evaluation.', error: error.message });
  }
});

app.get('/api/evaluations/student/:studentId', async (req, res) => {
  try {
    const [rows] = await pool.query(
      'SELECT * FROM evaluations WHERE student_id = ? ORDER BY submitted_at DESC',
      [req.params.studentId]
    );

    res.json(rows.map((evaluation) => ({
      id: evaluation.id,
      studentId: evaluation.student_id,
      instructor: evaluation.instructor,
      subject: evaluation.subject,
      rating: evaluation.rating,
      comments: evaluation.comments,
      submittedAt: evaluation.submitted_at
    })));
  } catch (error) {
    res.status(500).json({ message: 'Failed to fetch evaluations.', error: error.message });
  }
});

app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ message: 'Unexpected server error.', error: err.message });
});

app.listen(PORT, () => {
  console.log(`Student evaluation API running on http://localhost:${PORT}`);
});
