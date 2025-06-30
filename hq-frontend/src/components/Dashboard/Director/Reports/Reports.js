import React, { useState } from 'react';
import api from '../../../../services/api';
import './Reports.css';

const HeadquartersSalesReport = () => {
    const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
    const [reportData, setReportData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const formatCurrency = (amount) => `$${Number(amount).toFixed(2)}`;

    const fetchHeadquartersReport = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.get(`/reports/overview/${selectedYear}-${selectedMonth}`);
            console.log("Server response:", response.data);
            setReportData(response.data);
        } catch (err) {
            console.error('Error fetching report:', err);
            if (err.response) {
                setError(`Failed to fetch report: ${err.response.status} - ${err.response.data}`);
            } else {
                setError('Network error or no response from server.');
            }
        } finally {
            setLoading(false);
        }
    };

    // ⬇️ Move renderTable BEFORE return()
    const renderTable = () => {
        const overview = reportData?.branchSalesOverview;
        if (!overview || Object.keys(overview).length === 0) {
            return (
                <div className="alert alert-info mt-4">
                    No data available for the selected month.
                </div>
            );
        }

        return (
            <div className="table-wrapper mt-4">
                <table className="data-table">
                    <thead>
                    <tr>
                        <th>Branch ID</th>
                        <th>Total Sales</th>
                        <th>Products Sold</th>
                        <th>Revenue</th>
                    </tr>
                    </thead>
                    <tbody>
                    {Object.entries(overview).map(([branchId, data]) => (
                        <tr key={branchId}>
                            <td>{branchId}</td>
                            <td>{data.totalBranchSales}</td>
                            <td>{data.totalBranchProductsSold}</td>
                            <td>{formatCurrency(data.totalBranchRevenue)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        );
    };

    // ⬇️ Return JSX after renderTable is declared
    return (
        <div className="component-container">
            <div className="component-header">
                <h1>Headquarters Sales Overview</h1>
                <p>Monthly sales performance across all branches</p>
            </div>

            <div className="card">
                <div className="card-header">
                    <h3>Generate Report</h3>
                </div>
                <div className="card-body">
                    <div className="date-toolbar">
                        <div className="date-selector">
                            <div className="form-group">
                                <label htmlFor="month-select">Month</label>
                                <select
                                    id="month-select"
                                    className="custom-select"
                                    value={selectedMonth}
                                    onChange={(e) => setSelectedMonth(parseInt(e.target.value))}
                                >
                                    {[...Array(12)].map((_, i) => (
                                        <option key={i + 1} value={i + 1}>
                                            {new Date(2000, i).toLocaleString('default', { month: 'long' })}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="form-group">
                                <label htmlFor="year-select">Year</label>
                                <select
                                    id="year-select"
                                    className="custom-select"
                                    value={selectedYear}
                                    onChange={(e) => setSelectedYear(parseInt(e.target.value))}
                                >
                                    {[...Array(5)].map((_, i) => {
                                        const year = new Date().getFullYear() - 2 + i;
                                        return (
                                            <option key={year} value={year}>
                                                {year}
                                            </option>
                                        );
                                    })}
                                </select>
                            </div>
                        </div>

                        <button className="btn btn-primary" onClick={fetchHeadquartersReport}>
                            Generate Report
                        </button>
                    </div>
                </div>
            </div>

            {loading && (
                <div className="loading-container mt-4">
                    <div className="loading-spinner"></div>
                    <div className="loading-text">Generating report...</div>
                </div>
            )}

            {error && (
                <div className="alert alert-danger mt-4">
                    <strong>Error:</strong> {error}
                </div>
            )}

            {reportData && (
                <>
                    <div className="card mt-4">
                        <div className="card-header">
                            <h3>Overall Totals</h3>
                        </div>
                        <div className="card-body">
                            <p><strong>Total Sales:</strong> {reportData.salesCount ?? 0}</p>
                            <p><strong>Total Products Sold:</strong> {reportData.totalProductsSold ?? 0}</p>
                            <p><strong>Total Revenue:</strong> {reportData.totalRevenue != null ? formatCurrency(reportData.totalRevenue) : '$0.00'}</p>
                        </div>
                    </div>

                    <div className="card mt-4">
                        <div className="card-header">
                            <h3>Sales by Branch</h3>
                        </div>
                        <div className="card-body">
                            {renderTable()}
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default HeadquartersSalesReport;
