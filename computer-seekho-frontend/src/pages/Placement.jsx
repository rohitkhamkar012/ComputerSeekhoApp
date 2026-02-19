import React, { useEffect, useState } from 'react';
import { Container, Tab, Tabs, Row, Col, Card, Table, Button, Form } from 'react-bootstrap';
import api from '../services/api';

const Placement = () => {
    const [recruiters, setRecruiters] = useState([]);
    const [allPlacements, setAllPlacements] = useState([]); // Store master list
    const [placements, setPlacements] = useState([]); // Store displayed list
    const [activeTab, setActiveTab] = useState('recruiters');
    const [filterMessage, setFilterMessage] = useState('');

    // Filters & Masters
    const [courses, setCourses] = useState([]);
    const [batches, setBatches] = useState([]);
    const [filters, setFilters] = useState({ search: '', courseId: '', batchId: '' });

    useEffect(() => {
        fetchRecruiters();
        fetchPlacements();
        fetchMasters();
    }, []);

    const fetchRecruiters = async () => {
        try {
            const response = await api.get('/recruiter/getAll');
            setRecruiters(response.data);
        } catch (error) {
            console.error('Error fetching recruiters:', error);
        }
    };

    const fetchPlacements = async () => {
        try {
            const response = await api.get('/placement/all');
            console.log("Placements Data:", response.data); // DEBUG Log
            setAllPlacements(response.data || []);
            setPlacements(response.data || []);
        } catch (error) {
            console.error('Error fetching placements:', error);
        }
    };

    const fetchMasters = async () => {
        try {
            const [cRes, bRes] = await Promise.all([
                api.get('/course/getAll'), // Ensure this matches your Controller
                api.get('/batch/all')      // Ensure this matches your Controller
            ]);
            setCourses(cRes.data || []);
            setBatches(bRes.data || []);
        } catch (error) {
            console.error("Failed to load filters", error);
        }
    };

    const handleRecruiterClick = (recruiter) => {
        // Filter placements for this recruiter
        const filtered = allPlacements.filter(p => p.recruiterID?.recruiterId === recruiter.recruiterId);
        setPlacements(filtered);
        setFilterMessage(`Showing students placed in ${recruiter.recruiterName}`);
        setFilters({ search: '', courseId: '', batchId: '' }); // Clear filters
        setActiveTab('placed_students');
    };

    const handleResetFilter = () => {
        setPlacements(allPlacements);
        setFilterMessage('');
        setFilters({ search: '', courseId: '', batchId: '' }); // Clear filters
    };

    // Filter Logic
    const getFilteredPlacements = () => {
        // If we are showing a specific recruiter's list (from clicking a card), we might want to respect that AND apply local filters?
        // Or should local filters override? 
        // Let's make local filters refine the CURRENT viewed list (which is `placements`).
        // BUT, `placements` is stateful for the Recruiter click. 
        // Better approach: Derived state from `placements` (which serves as the "Base List" for the tab).

        return placements.filter(p => {
            const matchName = p.studentID?.studentName.toLowerCase().includes(filters.search.toLowerCase());
            // Safe navigation for nested props in case they are null
            const matchCourse = filters.courseId ? p.studentID?.course?.courseId == filters.courseId : true;
            const matchBatch = filters.batchId ? p.batch?.batchId == filters.batchId : true;
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
            <h2 className="mb-4">Placements</h2>
            <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} id="placement-tabs" className="mb-3">
                <Tab eventKey="recruiters" title="Our Recruiters">
                    <Row>
                        {recruiters.map((recruiter) => (
                            <Col md={3} key={recruiter.recruiterId} className="mb-4">
                                <Card
                                    className="h-100 border-0 shadow-sm p-3"
                                    style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
                                    onClick={() => handleRecruiterClick(recruiter)}
                                    // Add hover effect via simple inline style or class
                                    onMouseOver={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
                                    onMouseOut={(e) => e.currentTarget.style.transform = 'scale(1)'}
                                >
                                    <Card.Img
                                        variant="top"
                                        src={recruiter.recruiterPhotoUrl || '/images/logo.jpg'}
                                        style={{ height: '100px', objectFit: 'contain' }}
                                    />
                                    <Card.Body className="text-center">
                                        <Card.Title>{recruiter.recruiterName}</Card.Title>
                                        <Card.Text>{recruiter.recruiterLocation}</Card.Text>
                                        <small className="text-primary">Click to view students</small>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Tab>
                <Tab eventKey="placed_students" title="Placed Students">

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

                    {filterMessage && (
                        <div className="d-flex justify-content-between align-items-center mb-3 alert alert-info">
                            <span>{filterMessage}</span>
                            <Button variant="outline-primary" size="sm" onClick={handleResetFilter}>Show All Students</Button>
                        </div>
                    )}

                    <Table striped bordered hover responsive className="align-middle">
                        <thead className="table-light">
                            <tr>
                                <th>Student Photo</th>
                                <th>Student Name</th>
                                <th>Batch</th>
                                <th>Company</th>
                            </tr>
                        </thead>
                        <tbody>
                            {getFilteredPlacements().length > 0 ? (
                                getFilteredPlacements().map((p) => (
                                    <tr key={p.placementID}>
                                        <td>
                                            <img
                                                src={p.studentID?.photoUrl || '/images/logo.jpg'}
                                                alt={p.studentID?.studentName}
                                                style={{ width: '50px', height: '50px', objectFit: 'cover', borderRadius: '50%' }}
                                                onError={(e) => { e.target.src = '/images/logo.jpg'; }}
                                            />
                                        </td>
                                        <td className="fw-bold">{p.studentID ? p.studentID.studentName : 'N/A'}</td>
                                        <td className="text-muted">{p.batch ? p.batch.batchName : 'N/A'}</td>
                                        <td className="fw-bold text-primary">{p.recruiterID ? p.recruiterID.recruiterName : 'N/A'}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="4" className="text-center text-muted">No placed students found for this selection.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Tab>
            </Tabs>
        </Container>
    );
};

export default Placement;
