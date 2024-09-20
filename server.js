const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const port = 3000; // or any port you prefer

app.use(cors());
app.use(bodyParser.json());

// Sample route to get filtered data
app.get('/api/games/filter', (req, res) => {
    const { startDate, endDate } = req.query;
    // Logic to filter data from your database based on the dates
    res.json({ message: 'Filtered data based on dates', startDate, endDate });
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
