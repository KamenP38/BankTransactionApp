-- Database: banking_system

-- DROP DATABASE IF EXISTS banking_system;
-- CREATE DATABASE banking_system3;

CREATE TABLE Employee(
    ID INT NOT NULL, 
    emp_password VARCHAR(50) NOT NULL,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    salary INT CHECK (salary >= 0),
    emp_type VARCHAR(15) CHECK (emp_type IN ('Manager', 'Teller' ,'Loan Specialist')),
    SSN NUMERIC(9, 0),
    address_ID INT,
    branch_ID INT,
    PRIMARY KEY (ID),
    FOREIGN KEY(address_ID) REFERENCES Address(address_ID),
    FOREIGN KEY(branch_ID) REFERENCES Branch(branch_ID)
);

CREATE TABLE Address(
    city VARCHAR(16),
    street VARCHAR(16),
    zip INT,
    country VARCHAR(16),
    state VARCHAR(16),
    address_ID INT NOT NULL,
    PRIMARY KEY (address_ID)
);

INSERT INTO Address VALUES ('Chicago', 'Halsted', '60416', 'USA', 'IL', 3);
INSERT INTO BRANCH VALUES (3, 3);


CREATE TABLE Customer(
    ID INT NOT NULL,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    cust_password VARCHAR(50) NOT NULL,
    address_ID INT,
    branch_ID INT,
    PRIMARY KEY (ID),
    FOREIGN KEY(address_ID) REFERENCES Address(address_ID),
    FOREIGN KEY(branch_ID) REFERENCES Branch(branch_ID)
);

INSERT INTO Customer VALUES (0, 'Kamen', 'Petkov', 'test', 0, 0);

CREATE TABLE Branch(
    branch_ID INT NOT NULL,
    address_ID INT,
    PRIMARY KEY (branch_ID),
    FOREIGN KEY(address_ID) REFERENCES Address(address_ID)
);

INSERT INTO Branch VALUES (0, 0);

CREATE TABLE Accounts_And_Owners(
    account_num INT,
    ID INT,
    PRIMARY KEY (account_num, ID),
    FOREIGN KEY (ID) REFERENCES Customer(ID),
    FOREIGN KEY (account_num) REFERENCES Accounts(account_num)
);

CREATE TABLE Accounts(
    account_num INT NOT NULL,
    balance NUMERIC(8, 2) NOT NULL,
    account_name VARCHAR(50),
    PRIMARY KEY (account_num),
    FOREIGN KEY (account_name) REFERENCES Account_Types(account_name)
);

CREATE TABLE Account_Types(
    account_name VARCHAR(50),
    account_type VARCHAR(8) CHECK(account_type IN ('Checking', 'Savings')),
    interest_rate FLOAT(10),
    neg_balance BOOLEAN,
    overdraft_fee NUMERIC(8, 2),
    monthly_fee NUMERIC(8, 2),
    PRIMARY KEY (account_name)
);

CREATE TABLE Transactions(
    trans_type VARCHAR(10) CHECK(trans_type IN ('Deposit', 'Withdrawal', 'Transfer')),
    amount NUMERIC(8, 2),
    description VARCHAR(1000),
    trans_ID INT NOT NULL,
    date DATE,
    account_num INT,
    PRIMARY KEY (trans_ID),
    FOREIGN KEY (account_num) REFERENCES Accounts(account_num)
);

CREATE SEQUENCE customers_sequence
    START WITH 1
    INCREMENT BY 1
    NO CYCLE
    OWNED BY Customer.ID;
    
CREATE SEQUENCE employee_sequence
    START WITH 1
    INCREMENT BY 1
    NO CYCLE
    OWNED BY Employee.ID;
    
CREATE SEQUENCE address_sequence
	START WITH 4
	INCREMENT BY 1
	NO CYCLE
	OWNED BY ADDRESS.address_ID;
	
	
CREATE SEQUENCE account_sequence
	START WITH 1000
	INCREMENT BY 1
	NO CYCLE
	OWNED BY ADDRESS.address_ID;	

	

