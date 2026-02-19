import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Alert, Card, Spinner, Modal, Form } from 'react-bootstrap';
import api from '../../services/api';

const Students = () => {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    // Filters & Masters
    const [courses, setCourses] = useState([]);
    const [batches, setBatches] = useState([]);
    const [filters, setFilters] = useState({ search: '', courseId: '', batchId: '' });

    // Placement Modal State
    const [showModal, setShowModal] = useState(false);
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [recruiters, setRecruiters] = useState([]);
    const [selectedRecruiterId, setSelectedRecruiterId] = useState('');
    const [placedStudentIds, setPlacedStudentIds] = useState(new Set());

    useEffect(() => {
        fetchInitialData();
    }, []);

    const fetchInitialData = async () => {
        try {
            setLoading(true);
            const [sRes, rRes, pRes, cRes, bRes] = await Promise.all([
                api.get('/student/getAll'),
                api.get('/recruiter/getAll'),
                api.get('/placement/all'),
                api.get('/course/getAll'),
                api.get('/batch/all')
            ]);

            // Set Students
            setStudents(sRes.data || []);

            // Set Recruiters
            setRecruiters(rRes.data || []);

            // Set Placements
            const ids = new Set((pRes.data || []).map(p => p.studentID.studentId));
            setPlacedStudentIds(ids);

            // Set Masters
            setCourses(cRes.data || []);
            setBatches(bRes.data || []);

        } catch (err) {
            console.error("Failed to fetch data", err);
            setError('Failed to load data. Please check backend.');
        } finally {
            setLoading(false);
        }
    };

    const fetchPlacements = async () => {
        try {
            const response = await api.get('/placement/all');
            const ids = new Set(response.data.map(p => p.studentID.studentId));
            setPlacedStudentIds(ids);
        } catch (err) {
            console.error("Failed to fetch placements", err);
        }
    }

    const handlePlaceClick = (student) => {
        setSelectedStudent(student);
        setShowModal(true);
    };

    const handlePlacementSubmit = async () => {
        if (!selectedRecruiterId) {
            alert("Please select a recruiter");
            return;
        }
        if (!selectedStudent || !selectedStudent.batch) {
            alert("Student or Batch data missing");
            return;
        }

        const payload = {
            studentID: { studentId: selectedStudent.studentId },
            recruiterID: { recruiterId: parseInt(selectedRecruiterId) },
            batch: { batchId: selectedStudent.batch.batchId }
        };

        try {
            await api.post('/placement/add', payload);
            alert("Student Placed Successfully!");
            setShowModal(false);
            setSelectedRecruiterId('');
            fetchPlacements(); // Refresh list to update buttons
        } catch (err) {
            console.error(err);
            alert("Failed to place student");
        }
    };

    const downloadPdf = async (studentId, studentName) => {
        try {
            const response = await api.get(`/student/pdf/${studentId}`, {
                responseType: 'blob',
            });
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `${studentName ? studentName.replace(/\s+/g, '_') : 'Student'}_Profile.pdf`);
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (err) {
            console.error("PDF Download failed", err);
            alert("Failed to download PDF. Please try again.");
        }
    };

    // Filter Logic
    const getFilteredStudents = () => {
        return students.filter(s => {
            const matchName = s.studentName.toLowerCase().includes(filters.search.toLowerCase());
            const matchCourse = filters.courseId ? s.course?.courseId == filters.courseId : true;
            const matchBatch = filters.batchId ? s.batch?.batchId == filters.batchId : true;
            return matchName && matchCourse && matchBatch;
        });
    };

    // Derived Batches based on Selected Course
    const getFilteredBatches = () => {
        if (!filters.courseId) return batches;
        return batches.filter(b => b.course && b.course.courseId == filters.courseId);
    };

    return (
        <Container className="mt-4">
            <h2 className="mb-4">Registered Students</h2>

            {/* Filters */}
            <div className="row g-2 mb-3 bg-light p-3 rounded border">
                <div className="col-md-4">
                    <Form.Label className="fw-bold small">Search Student</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Enter Name..."
                        value={filters.search}
                        onChange={e => setFilters({ ...filters, search: e.target.value })}
                    />
                </div>
                <div className="col-md-4">
                    <Form.Label className="fw-bold small">Filter by Course</Form.Label>
                    <Form.Select
                        value={filters.courseId}
                        onChange={e => setFilters({ ...filters, courseId: e.target.value, batchId: '' })}
                    >
                        <option value="">All Courses</option>
                        {courses.map(c => <option key={c.courseId} value={c.courseId}>{c.courseName}</option>)}
                    </Form.Select>
                </div>
                <div className="col-md-4">
                    <Form.Label className="fw-bold small">Filter by Batch</Form.Label>
                    <Form.Select
                        value={filters.batchId}
                        onChange={e => setFilters({ ...filters, batchId: e.target.value })}
                        disabled={!filters.courseId && getFilteredBatches().length === batches.length && batches.length > 0}
                    >
                        <option value="">All Batches</option>
                        {getFilteredBatches().map(b => <option key={b.batchId} value={b.batchId}>{b.batchName}</option>)}
                    </Form.Select>
                </div>
            </div>

            {error && <Alert variant="danger">{error}</Alert>}

            {loading ? (
                <div className="text-center">
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
                </div>
            ) : (
                <Card className="shadow-sm">
                    <Card.Body>
                        <Table striped bordered hover responsive className="align-middle">
                            <thead className="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Mobile</th>
                                    <th>Course</th>
                                    <th>Batch</th>
                                    <th>Payment Due</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {getFilteredStudents().length > 0 ? (
                                    getFilteredStudents().map((student) => (
                                        <tr key={student.studentId}>
                                            <td>{student.studentId}</td>
                                            <td className="fw-bold"> {student.studentName} </td>
                                            <td>{student.studentMobile}</td>
                                            <td>{student.course ? student.course.courseName : 'N/A'}</td>
                                            <td>{student.batch ? student.batch.batchName : 'N/A'}</td>
                                            <td>
                                                {student.paymentDue > 0 ?
                                                    <span className="text-danger fw-bold">₹{student.paymentDue}</span> :
                                                    <span className="text-success fw-bold">Paid</span>
                                                }
                                            </td>
                                            <td>
                                                <Button
                                                    variant="outline-danger"
                                                    size="sm"
                                                    className="me-2"
                                                    onClick={() => downloadPdf(student.studentId, student.studentName)}
                                                >
                                                    PDF
                                                </Button>
                                                {placedStudentIds.has(student.studentId) ? (
                                                    <Button variant="secondary" size="sm" disabled>Placed</Button>
                                                ) : (
                                                    <Button
                                                        variant="success"
                                                        size="sm"
                                                        onClick={() => handlePlaceClick(student)}
                                                    >
                                                        Place
                                                    </Button>
                                                )}
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan="7" className="text-center text-muted">No students found matching criteria.</td>
                                    </tr>
                                )}
                            </tbody>
                        </Table>
                    </Card.Body>
                </Card>
            )}

            {/* Placement Modal */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Place Student: {selectedStudent?.studentName}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Select Company</Form.Label>
                            <Form.Select
                                value={selectedRecruiterId}
                                onChange={(e) => setSelectedRecruiterId(e.target.value)}
                            >
                                <option value="">-- Select Recruiter --</option>
                                {recruiters.map((r) => (
                                    <option key={r.recruiterId} value={r.recruiterId}>
                                        {r.recruiterName} ({r.recruiterLocation})
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <p>
                            <strong>Batch:</strong> {selectedStudent?.batch?.batchName || 'N/A'}
                        </p>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handlePlacementSubmit}>
                        Confirm Placement
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default Students;
