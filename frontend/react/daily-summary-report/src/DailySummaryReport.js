/**
 * © 2024 Jerry Tan. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jerry Tan
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms under which this software
 * was distributed or otherwise published, and solely for the prior express purposes
 * explicitly communicated and agreed upon, and only for those specific permissible purposes.
 *
 * This software is provided "AS IS," without a warranty of any kind. All express or implied
 * conditions, representations, and warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose, or non-infringement, are disclaimed, except to the extent
 * that such disclaimers are held to be legally invalid.
 */
import React, { useEffect, useState, useRef } from 'react';
import {
    Table, TableBody, TableCell, TableContainer, TableHead,
    TableRow, Paper, TableSortLabel, Typography, CircularProgress,
    Zoom
} from '@mui/material';

/**
 * DailySummaryReport component displays a daily summary report
 * fetched from an API or uses fallback data. It includes a table
 * with sortable columns and a transition effect upon loading.
 */
function DailySummaryReport() {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [orderBy, setOrderBy] = useState('clientInformation');
    const [order, setOrder] = useState('asc');
    const tableRef = useRef(null);

    /**
     * useEffect hook to fetch data from the API or fallback to default data.
     * Also handles errors during fetch.
     */
    useEffect(() => {
        console.log('Fetching data...');
        const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/get_daily_summary_report';

        fetch(apiUrl)
            .then(response => response.json())
            .then(apiData => {
                if (apiData && apiData.length > 0) {
                    setData(apiData);
                } else {
                    setData([
                        { clientInformation: "CL432100030001", productInformation: "CMEFUN120100910", totalTransactionAmount: -79.0 },
                        { clientInformation: "CL123400030001", productInformation: "CMEFUN120100910", totalTransactionAmount: 285.0 },
                        { clientInformation: "CL123400030001", productInformation: "CMEFUNK20100910", totalTransactionAmount: -215.0 },
                        { clientInformation: "CL123400020001", productInformation: "SGXFUNK20100910", totalTransactionAmount: -52.0 },
                        { clientInformation: "CL432100020001", productInformation: "SGXFUNK20100910", totalTransactionAmount: 46.0 }
                    ]);
                }
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
                setData([
                    { clientInformation: "CL432100030001", productInformation: "CMEFUN120100910", totalTransactionAmount: -79.0 },
                    { clientInformation: "CL123400030001", productInformation: "CMEFUN120100910", totalTransactionAmount: 285.0 },
                    { clientInformation: "CL123400030001", productInformation: "CMEFUNK20100910", totalTransactionAmount: -215.0 },
                    { clientInformation: "CL123400020001", productInformation: "SGXFUNK20100910", totalTransactionAmount: -52.0 },
                    { clientInformation: "CL432100020001", productInformation: "SGXFUNK20100910", totalTransactionAmount: 46.0 }
                ]);
                setLoading(false);
            });
    }, []);

    /**
     * useEffect hook to auto-scroll to the table after loading data.
     */
    useEffect(() => {
        if (!loading && tableRef.current) {
            // Smooth scroll to the table
            tableRef.current.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }, [loading]);

    /**
     * handleSort function sorts the data based on the specified property.
     * @param {string} property - The property to sort by.
     */
    const handleSort = (property) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    };

    console.log('Rendering component with data:', data);

    return (
        <div style={{ padding: '20px' }}>
            <Zoom in={!loading} timeout={1000}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Daily Summary Report
                </Typography>
            </Zoom>
            {loading ? (
                <CircularProgress style={{ marginTop: '20px' }} />
            ) : (
                <Zoom in={!loading} timeout={1000}>
                    <TableContainer component={Paper} style={{ marginTop: '20px' }} ref={tableRef}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>
                                        <TableSortLabel
                                            active={orderBy === 'clientInformation'}
                                            direction={orderBy === 'clientInformation' ? order : 'asc'}
                                            onClick={() => handleSort('clientInformation')}
                                        >
                                            Client Information
                                        </TableSortLabel>
                                    </TableCell>
                                    <TableCell>
                                        <TableSortLabel
                                            active={orderBy === 'productInformation'}
                                            direction={orderBy === 'productInformation' ? order : 'asc'}
                                            onClick={() => handleSort('productInformation')}
                                        >
                                            Product Information
                                        </TableSortLabel>
                                    </TableCell>
                                    <TableCell>
                                        <TableSortLabel
                                            active={orderBy === 'totalTransactionAmount'}
                                            direction={orderBy === 'totalTransactionAmount' ? order : 'asc'}
                                            onClick={() => handleSort('totalTransactionAmount')}
                                        >
                                            Total Transaction Amount
                                        </TableSortLabel>
                                    </TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data.length > 0 ? (
                                    data.sort((a, b) => {
                                        if (order === 'asc') {
                                            return a[orderBy] < b[orderBy] ? -1 : 1;
                                        } else {
                                            return a[orderBy] > b[orderBy] ? -1 : 1;
                                        }
                                    }).map((row, index) => (
                                        <TableRow key={index} style={{ backgroundColor: index % 2 === 0 ? '#f0f8ff' : '#e6ffe6' }}>
                                            <TableCell>{row.clientInformation}</TableCell>
                                            <TableCell>{row.productInformation}</TableCell>
                                            <TableCell>{row.totalTransactionAmount}</TableCell>
                                        </TableRow>
                                    ))
                                ) : (
                                    <TableRow>
                                        <TableCell colSpan="3" align="center">No data available</TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Zoom>
            )}
        </div>
    );
}

export default DailySummaryReport;
