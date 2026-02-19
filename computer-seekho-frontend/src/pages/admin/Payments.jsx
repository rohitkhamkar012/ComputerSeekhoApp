import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Modal, Form, Alert, Spinner, Badge } from 'react-bootstrap';
import api from '../../services/api';

const Payments = () => {
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [students, setStudents] = useState([]);

    // Filters & Masters
    const [courses, setCourses] = useState([]);
    const [batches, setBatches] = useState([]);
    const [filters, setFilters] = useState({ search: '', courseId: '', batchId: '' });

    const [formData, setFormData] = useState({
        studentId: '',
        amount: '',
        paymentTypeId: 1, // Default Cash (assuming 1)
        paymentDate: new Date().toISOString().split('T')[0]
    });
    const [status, setStatus] = useState({ type: '', msg: '' });

    // History Modal
    const [showHistoryModal, setShowHistoryModal] = useState(false);
    const [selectedStudentHistory, setSelectedStudentHistory] = useState([]);
    const [selectedStudentName, setSelectedStudentName] = useState('');
    const [selectedStudentId, setSelectedStudentId] = useState(null);
    const [historyStats, setHistoryStats] = useState({ totalFees: 0, totalPaid: 0, due: 0 });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            const [sRes, cRes, bRes] = await Promise.all([
                api.get('/student/getAll'),
                api.get('/course/getAll'),
                api.get('/batch/all') // Corrected API Endpoint
            ]);

            // Sort Students by Name
            const sortedStudents = (sRes.data || []).sort((a, b) => a.studentName.localeCompare(b.studentName));
            setStudents(sortedStudents);

            setCourses(cRes.data || []);
            setBatches(bRes.data || []);
        } catch (error) {
            console.error("Initialization Error:", error);
            setStatus({ type: 'danger', msg: 'Failed to load data. Please check backend.' });
        } finally {
            setLoading(false);
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

    const handleAddPayment = async () => {
        if (!formData.studentId || !formData.amount) {
            alert("Please select student and amount");
            return;
        }

        const payload = {
            student: { studentId: parseInt(formData.studentId) },
            amount: parseFloat(formData.amount),
            paymentDate: formData.paymentDate,
            paymentTypeId: { paymentTypeId: parseInt(formData.paymentTypeId) }
        };

        try {
            await api.post('/payment/add', payload);
            setStatus({ type: 'success', msg: 'Payment Recorded Successfully' });
            setShowModal(false);
            // Refresh students to update "Due" amount
            const res = await api.get('/student/getAll');
            setStudents(res.data.sort((a, b) => a.studentName.localeCompare(b.studentName)));

            // Allow manual refresh of history if open
            if (showHistoryModal && selectedStudentId) {
                // Optional: Re-fetch history if window is open
                const histRes = await api.get(`/payment/getByStudent/${selectedStudentId}`);
                const history = histRes.data;
                setSelectedStudentHistory(history);
                const totalPaid = history.reduce((sum, p) => sum + p.amount, 0);
                const s = res.data.find(st => st.studentId === selectedStudentId);
                setHistoryStats({ totalFees: totalPaid + s.paymentDue, totalPaid, due: s.paymentDue });
            }
            // Reset form
            setFormData({ ...formData, studentId: '', amount: '' });
        } catch (error) {
            console.error(error);
            setStatus({ type: 'danger', msg: 'Failed to record payment.' });
        }
    };

    const handleStudentClick = async (student) => {
        try {
            setSelectedStudentName(student.studentName);
            setSelectedStudentId(student.studentId);

            // 1. Get Payment History
            const res = await api.get(`/payment/getByStudent/${student.studentId}`);
            const history = res.data;
            setSelectedStudentHistory(history);

            // 2. Calculate Stats
            const totalPaid = history.reduce((sum, p) => sum + p.amount, 0);

            // Use the student object from the list to get the latest Due info
            const currentDue = student.paymentDue;
            const totalFees = totalPaid + currentDue;

            setHistoryStats({ totalFees, totalPaid, due: currentDue });
            setShowHistoryModal(true);

        } catch (error) {
            console.error("Failed to fetch history", error);
            alert("Could not fetch payment history.");
        }
    };

    const openPaymentModalFromLedger = () => {
        setShowHistoryModal(false);
        const stu = students.find(s => s.studentId === selectedStudentId);
        setFormData({
            ...formData,
            studentId: selectedStudentId,
            amount: stu ? stu.paymentDue : ''
        });
        setShowModal(true);
    };

    return (
        <Container className="mt-4">
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>Payment Ledger Registry</h2>
                <Button variant="primary" onClick={() => {
                    setFormData({ ...formData, studentId: '', amount: '' });
                    setShowModal(true);
                }}>Add New Payment</Button>
            </div>

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

            {status.msg && <Alert variant={status.type} onClose={() => setStatus({ type: '', msg: '' })} dismissible>{status.msg}</Alert>}

            {loading ? <div className="text-center p-5"><Spinner animation="border" /></div> : (
                <Table hover responsive className="align-middle">
                    <thead className="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Student Name</th>
                            <th>Course</th>
                            <th>Batch</th>
                            <th>Total Due</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {getFilteredStudents().length > 0 ? (
                            getFilteredStudents().map(s => (
                                <tr key={s.studentId}>
                                    <td>{s.studentId}</td>
                                    <td className="fw-bold">{s.studentName}</td>
                                    <td>{s.course?.courseName || '-'}</td>
                                    <td>{s.batch?.batchName || '-'}</td>
                                    <td>
                                        {s.paymentDue > 0 ?
                                            <Badge bg="danger">₹{s.paymentDue}</Badge> :
                                            <Badge bg="success">Paid</Badge>
                                        }
                                    </td>
                                    <td>
                                        <Button
                                            variant="outline-primary"
                                            size="sm"
                                            onClick={() => handleStudentClick(s)}
                                        >
                                            View History
                                        </Button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr><td colSpan="6" className="text-center text-muted">No students found matching criteria.</td></tr>
                        )}
                    </tbody>
                </Table>
            )}

            {/* Manual Payment Modal */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton><Modal.Title>Record Payment</Modal.Title></Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Student</Form.Label>
                            <Form.Select
                                value={formData.studentId}
                                onChange={(e) => {
                                    const sid = e.target.value;
                                    const stu = students.find(s => s.studentId == sid);
                                    setFormData({ ...formData, studentId: sid, amount: stu ? stu.paymentDue : '' });
                                }}
                            >
                                <option value="">-- Select Student (Only Pending Fees) --</option>
                                {students
                                    .filter(s => s.paymentDue > 0)
                                    .map(s => (
                                        <option key={s.studentId} value={s.studentId}>
                                            {s.studentName} (Due: ₹{s.paymentDue})
                                        </option>
                                    ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Amount</Form.Label>
                            <Form.Control type="number" value={formData.amount} onChange={e => setFormData({ ...formData, amount: e.target.value })} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Payment Type</Form.Label>
                            <Form.Select value={formData.paymentTypeId} onChange={e => setFormData({ ...formData, paymentTypeId: e.target.value })}>
                                <option value="1">Cash</option>
                                <option value="2">Cheque</option>
                                <option value="3">Online Transfer</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Date</Form.Label>
                            <Form.Control type="date" value={formData.paymentDate} onChange={e => setFormData({ ...formData, paymentDate: e.target.value })} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>Cancel</Button>
                    <Button variant="success" onClick={handleAddPayment}>Record Payment</Button>
                </Modal.Footer>
            </Modal>

            {/* History Modal */}
            <Modal show={showHistoryModal} onHide={() => setShowHistoryModal(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>Payment Ledger: {selectedStudentName}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="row mb-4 text-center">
                        <div className="col-md-4">
                            <div className="card bg-light border-0 p-3">
                                <h6 className="text-muted">Total Course Fee</h6>
                                <h4 className="fw-bold">₹{historyStats.totalFees}</h4>
                            </div>
                        </div>
                        <div className="col-md-4">
                            <div className="card bg-success text-white border-0 p-3">
                                <h6 className="opacity-75">Total Paid</h6>
                                <h4 className="fw-bold">₹{historyStats.totalPaid}</h4>
                            </div>
                        </div>
                        <div className="col-md-4">
                            <div className="card bg-danger text-white border-0 p-3">
                                <h6 className="opacity-75">Current Due</h6>
                                <h4 className="fw-bold">₹{historyStats.due}</h4>
                            </div>
                        </div>
                    </div>

                    <h5 className="mb-3">Transaction History</h5>
                    <Table striped bordered responsive size="sm">
                        <thead className="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Date</th>
                                <th>Mode</th>
                                <th>Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            {selectedStudentHistory.length > 0 ? (
                                selectedStudentHistory.map(p => (
                                    <tr key={p.paymentId}>
                                        <td>{p.paymentId}</td>
                                        <td>{p.paymentDate}</td>
                                        <td>{p.paymentTypeId?.paymentTypeDesc || p.paymentType?.paymentTypeDesc || 'Cash'}</td>
                                        <td className="fw-bold text-success">+ ₹{p.amount}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr><td colSpan="4" className="text-center">No Transactions Found</td></tr>
                            )}
                        </tbody>
                    </Table>
                </Modal.Body>
                <Modal.Footer>
                    <div className="w-100 d-flex justify-content-between">
                        <Button variant="secondary" onClick={() => setShowHistoryModal(false)}>Close</Button>
                        <Button variant="primary" onClick={openPaymentModalFromLedger} disabled={historyStats.due <= 0}>
                            Record New Payment
                        </Button>
                    </div>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default Payments;
