const express = require('express');
const cors = require('cors');
const app = express();
const port = process.env.PORT || 8081;

// Ermöglicht Cross-Origin-Anfragen
app.use(cors());

app.get('/auszahlungen', (req, res) => {
    res.json([
        { amount: 12.5, category: 'Lebensmittel', date: '2025-11-08' },
        { amount: 40, category: 'Kleidung', date: '2025-11-07' },
        { amount: 5, category: 'Fahrtkosten', date: '2025-11-06' }
    ]);
});

app.listen(port, () => {
    console.log(`Server läuft auf http://localhost:${port}`);
});
