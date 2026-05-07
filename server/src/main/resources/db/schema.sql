-- ============================================================
-- Fintech Dashboard - Database Schema (Database-First)
--
-- Naming Convention:
--   Table names  : tb prefix, all lowercase      (e.g. tbusers)
--   Primary keys : IDP suffix, CapitalCase        (e.g. UserIDP)
--   Foreign keys : IDF suffix, CapitalCase        (e.g. UserIDF)
--   Column names : CapitalCase, no underscores    (e.g. FullName)
--
-- Audit columns (from BaseAuditEntity — on every table):
--   CreatedAt         TIMESTAMP NOT NULL     -- auto-set on INSERT, never changes
--   LastModified      TIMESTAMP NULL         -- auto-set on UPDATE, null until first update
--   CreatedByIDF      INT    NOT NULL     -- user who created the record
--   LastModifiedByIDF INT    NULL         -- user who last modified, null until first update
-- ============================================================

-- ============================================================
-- tbcurrencies
-- ============================================================
CREATE TABLE
IF NOT EXISTS tbcurrencies
(
    CurrencyIDP         INT          PRIMARY KEY AUTO_INCREMENT,
    Code                VARCHAR
(3)      NOT NULL UNIQUE,
    Name                VARCHAR
(100)    NOT NULL,
    Symbol              VARCHAR
(5)      NOT NULL,
    Decimals            INT             NOT NULL DEFAULT 2,
    IsActive            BOOLEAN         NOT NULL DEFAULT TRUE,
    ExchangeRate        NUMERIC
(18, 6)  NOT NULL DEFAULT 1.000000,
    -- Audit columns
    CreatedAt           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModified        TIMESTAMP       NULL ON
UPDATE CURRENT_TIMESTAMP,
    CreatedByIDF        INT
NOT NULL DEFAULT 0,
    LastModifiedByIDF   INT          NULL
);

INSERT INTO tbcurrencies
    (Code, Name, Symbol, Decimals, IsActive, ExchangeRate, CreatedAt, CreatedByIDF)
VALUES
    ('USD', 'US Dollar', '$', 2, TRUE, 1.000000, NOW(), 0),
    ('EUR', 'Euro', '€', 2, TRUE, 0.920000, NOW(), 0),
    ('GBP', 'British Pound', '£', 2, TRUE, 0.790000, NOW(), 0),
    ('INR', 'Indian Rupee', '₹', 2, TRUE, 83.120000, NOW(), 0),
    ('JPY', 'Japanese Yen', '¥', 0, TRUE, 149.500000, NOW(), 0);

-- ============================================================
-- tbwallets - ADD currencyidf column
-- Note: Run separately after adding tbcurrencies table
-- ALTER TABLE tbwallets ADD COLUMN CurrencyIDF INT NULL REFERENCES tbcurrencies(CurrencyIDP);
-- ALTER TABLE tbwallets ADD COLUMN CurrencyCode VARCHAR(3) NOT NULL DEFAULT 'USD';

-- ============================================================
-- tbusers
-- ============================================================
CREATE TABLE
IF NOT EXISTS tbusers
(
    UserIDP             INT          PRIMARY KEY AUTO_INCREMENT,
    Email               VARCHAR
(255)    NOT NULL UNIQUE,
    FullName            VARCHAR
(255)    NOT NULL,
    Password            VARCHAR
(255)    NOT NULL,
    Phone               VARCHAR
(50),
    KycStatus           VARCHAR
(20)     NOT NULL DEFAULT 'UNVERIFIED',
    AccountTier         VARCHAR
(20)     NOT NULL DEFAULT 'FREE',
    TransactionLimit    NUMERIC
(18, 2)  NOT NULL DEFAULT 1000.00,
    Country             VARCHAR
(100),
    -- Audit columns
    CreatedAt           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModified        TIMESTAMP       NULL ON
UPDATE CURRENT_TIMESTAMP,
    CreatedByIDF        INT
NOT NULL DEFAULT 0,
    LastModifiedByIDF   INT          NULL,
    CONSTRAINT chk_KycStatus CHECK
(KycStatus IN
('UNVERIFIED','PENDING','BASIC','PREMIUM','REJECTED')),
    CONSTRAINT chk_AccountTier CHECK
(AccountTier IN
('FREE','BASIC','PREMIUM'))
);

-- ============================================================
-- tbwallets
-- ============================================================
CREATE TABLE
IF NOT EXISTS tbwallets
(
    WalletIDP           INT          PRIMARY KEY AUTO_INCREMENT,
    UserIDF             INT          NOT NULL,
    Currency            VARCHAR
(10)     NOT NULL DEFAULT 'USD',
    Balance             NUMERIC
(18, 2)  NOT NULL DEFAULT 0.00,
    AvailableBalance    NUMERIC
(18, 2)  NOT NULL DEFAULT 0.00,
    Status              VARCHAR
(20)     NOT NULL DEFAULT 'ACTIVE',
    WalletNumber        VARCHAR
(100)    NOT NULL UNIQUE,
    -- Audit columns
    CreatedAt           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModified        TIMESTAMP       NULL ON
UPDATE CURRENT_TIMESTAMP,
    CreatedByIDF        INT
NOT NULL,
    LastModifiedByIDF   INT          NULL,
    CONSTRAINT fk_tbwallets_useridf FOREIGN KEY
(UserIDF) REFERENCES tbusers
(UserIDP) ON
DELETE CASCADE,
    CONSTRAINT chk_WalletStatus CHECK
(Status IN
('ACTIVE','FROZEN','CLOSED'))
);

-- ============================================================
-- tbtransactions
-- ============================================================
CREATE TABLE
IF NOT EXISTS tbtransactions
(
    TransactionIDP      INT          PRIMARY KEY AUTO_INCREMENT,
    WalletIDF           INT          NOT NULL,
    Type                VARCHAR
(20)     NOT NULL,
    Amount              NUMERIC
(18, 2)  NOT NULL,
    Currency            VARCHAR
(10)     NOT NULL DEFAULT 'USD',
    Status              VARCHAR
(20)     NOT NULL DEFAULT 'PENDING',
    Description         TEXT,
    Reference           VARCHAR
(255)    NOT NULL UNIQUE,
    CounterpartyName    VARCHAR
(255),
    CounterpartyAccount VARCHAR
(255),
    BalanceBefore       NUMERIC
(18, 2)  NOT NULL,
    BalanceAfter        NUMERIC
(18, 2)  NOT NULL,
    Metadata            JSON,
    -- Audit columns
    CreatedAt           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModified        TIMESTAMP       NULL ON
UPDATE CURRENT_TIMESTAMP,
    CreatedByIDF        INT
NOT NULL,
    LastModifiedByIDF   INT          NULL,
    CONSTRAINT fk_tbtransactions_walletidf FOREIGN KEY
(WalletIDF) REFERENCES tbwallets
(WalletIDP) ON
DELETE RESTRICT,
    CONSTRAINT chk_TransactionType CHECK
(Type IN
('DEBIT','CREDIT','TRANSFER','DEPOSIT','WITHDRAWAL')),
    CONSTRAINT chk_TransactionStatus CHECK
(Status IN
('PENDING','PROCESSING','COMPLETED','FAILED','REFUNDED'))
);

-- ============================================================
-- tbriskflag
-- ============================================================
CREATE TABLE
IF NOT EXISTS tbriskflag
(
    RiskFlagIDP         INT          PRIMARY KEY AUTO_INCREMENT,
    UserIDF             INT          NOT NULL,
    TransactionIDF      INT          NULL,
    Type                VARCHAR
(50)     NOT NULL,
    Severity            VARCHAR
(20)     NOT NULL,
    Status              VARCHAR
(30)     NOT NULL DEFAULT 'OPEN',
    Description         TEXT            NOT NULL,
    ResolvedAt          TIMESTAMP       NULL,
    ResolvedBy          VARCHAR
(255),
    Resolution          TEXT,
    -- Audit columns
    CreatedAt           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModified        TIMESTAMP       NULL ON
UPDATE CURRENT_TIMESTAMP,
    CreatedByIDF        INT
NOT NULL,
    LastModifiedByIDF   INT          NULL,
    CONSTRAINT fk_tbriskflag_useridf FOREIGN KEY
(UserIDF) REFERENCES tbusers
(UserIDP) ON
DELETE CASCADE,
    CONSTRAINT fk_tbriskflag_transactionidf FOREIGN KEY
(TransactionIDF) REFERENCES tbtransactions
(TransactionIDP) ON
DELETE
SET NULL
,
    CONSTRAINT chk_RiskFlagType CHECK
(Type IN
('VELOCITY_CHECK','HIGH_VALUE','GEOFENCE','SUSPICIOUS_PATTERN','AML_ALERT')),
    CONSTRAINT chk_RiskSeverity CHECK
(Severity IN
('LOW','MEDIUM','HIGH','CRITICAL')),
    CONSTRAINT chk_RiskFlagStatus CHECK
(Status IN
('OPEN','INVESTIGATING','RESOLVED','FALSE_POSITIVE'))
);

-- ============================================================
-- tbnotifications
-- ============================================================
CREATE TABLE
IF NOT EXISTS tbnotifications
(
    NotificationIDP     INT          PRIMARY KEY AUTO_INCREMENT,
    UserIDF             INT          NOT NULL,
    Type                VARCHAR
(50)     NOT NULL,
    Title               VARCHAR
(255)    NOT NULL,
    Message             TEXT            NOT NULL,
    IsRead              BOOLEAN         NOT NULL DEFAULT FALSE,
    -- Audit columns
    CreatedAt           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModified        TIMESTAMP       NULL ON
UPDATE CURRENT_TIMESTAMP,
    CreatedByIDF        INT
NOT NULL,
    LastModifiedByIDF   INT          NULL,
    CONSTRAINT fk_tbnotifications_useridf FOREIGN KEY
(UserIDF) REFERENCES tbusers
(UserIDP) ON
DELETE CASCADE,
    CONSTRAINT chk_NotificationType CHECK
(Type IN
('HIGH_VALUE_TRANSACTION','RISK_ALERT','KYC_UPDATE','SYSTEM'))
);

-- ============================================================
-- Self-referential audit FKs for tbusers
-- ============================================================
ALTER TABLE tbusers
    ADD CONSTRAINT fk_tbusers_createdby
        FOREIGN KEY (CreatedByIDF) REFERENCES tbusers(UserIDP)
ON DELETE RESTRICT;

ALTER TABLE tbusers
    ADD CONSTRAINT fk_tbusers_lastmodifiedby
        FOREIGN KEY (LastModifiedByIDF) REFERENCES tbusers(UserIDP) ON DELETE SET NULL;

-- ============================================================
-- Indexes
-- ============================================================
CREATE INDEX idx_tbwallets_useridf             ON tbwallets(UserIDF);
CREATE INDEX idx_tbtransactions_walletidf      ON tbtransactions(WalletIDF);
CREATE INDEX idx_tbtransactions_status         ON tbtransactions(Status);
CREATE INDEX idx_tbtransactions_createdat      ON tbtransactions(CreatedAt DESC);
CREATE INDEX idx_tbriskflag_useridf            ON tbriskflag(UserIDF);
CREATE INDEX
IF NOT EXISTS idx_tbriskflag_transactionidf  ON tbriskflag
(TransactionIDF);
CREATE INDEX idx_tbriskflag_status             ON tbriskflag(Status);
CREATE INDEX idx_tbnotifications_useridf       ON tbnotifications(UserIDF);
CREATE INDEX idx_tbnotifications_isread        ON tbnotifications(IsRead);
