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
import React, { useEffect, useState } from 'react';

function DailySummaryReportEx() {
    const [data, setData] = useState([]);

    useEffect(() => {
        console.log('Fetching data...');
        setData([
            { clientInformation: "CL432100030001", productInformation: "CMEFUN120100910", totalTransactionAmount: -79.0 },
            { clientInformation: "CL123400030001", productInformation: "CMEFUN120100910", totalTransactionAmount: 285.0 },
            { clientInformation: "CL123400030001", productInformation: "CMEFUNK20100910", totalTransactionAmount: -215.0 },
            { clientInformation: "CL123400020001", productInformation: "SGXFUNK20100910", totalTransactionAmount: -52.0 },
            { clientInformation: "CL432100020001", productInformation: "SGXFUNK20100910", totalTransactionAmount: 46.0 }
        ]);
        console.log('Data set:', data);
    }, []);

    console.log('Rendering component with data:', data);

    return (
        <div>
            <h1>Daily Summary Report</h1>
            <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr>
                    <th>Client Information</th>
                    <th>Product Information</th>
                    <th>Total Transaction Amount</th>
                </tr>
                </thead>
                <tbody>
                {data.length > 0 ? (
                    data.map((row, index) => (
                        <tr key={index}>
                            <td>{row.clientInformation}</td>
                            <td>{row.productInformation}</td>
                            <td>{row.totalTransactionAmount}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="3" style={{ textAlign: 'center' }}>No data available</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default DailySummaryReportEx;
