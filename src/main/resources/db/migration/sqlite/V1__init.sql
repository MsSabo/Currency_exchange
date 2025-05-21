CREATE TABLE Currencies
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT,
    fullName TEXT,
    sign TEXT
);

CREATE TABLE ExchangeRates
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    baseCurrencyId INT NOT NULL,
    targetCurrencyId INT NOT NULL,
    rate NUMERIC,
    FOREIGN KEY(baseCurrencyId) REFERENCES Currencies(id),
    FOREIGN KEY(targetCurrencyId) REFERENCES Currencies(id)
);

CREATE UNIQUE INDEX id_currency_code
    ON Currencies(code);

CREATE UNIQUE INDEX exchange_pair
    ON ExchangeRates(basecurrencyid, targetcurrencyid);

INSERT INTO Currencies (id, code, fullName, sign) VALUES
(1, 'USD', 'United States Dollar', '$'),
(2, 'EUR', 'Euro', '€'),
(3, 'GBP', 'British Pound Sterling', '£'),
(4, 'JPY', 'Japanese Yen', '¥'),
(5, 'RUB', 'Russian Ruble', '₽');


INSERT INTO ExchangeRates (id, baseCurrencyId, targetCurrencyId, rate) VALUES
(1, 1, 2, 0.92),   -- USD → EUR
(2, 1, 3, 0.78),   -- USD → GBP
(3, 1, 4, 150.0),  -- USD → JPY
(4, 1, 5, 90.0),   -- USD → RUB

(5, 2, 1, 1.09),   -- EUR → USD
(6, 2, 3, 0.85),   -- EUR → GBP
(7, 2, 4, 162.0),  -- EUR → JPY
(8, 2, 5, 97.0),   -- EUR → RUB

(9, 5, 1, 0.011),  -- RUB → USD
(10, 5, 2, 0.0103);-- RUB → EUR