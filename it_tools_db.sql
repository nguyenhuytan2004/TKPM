DROP DATABASE IF EXISTS it_tools_db;
CREATE DATABASE it_tools_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE it_tools_db;

-- Bảng users (Lưu thông tin người dùng)
CREATE TABLE users (
    id MEDIUMINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role ENUM('USER', 'ADMIN', 'PREMIUM') DEFAULT 'USER',
    require_premium BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB;

INSERT INTO users (username, password_hash) VALUES 
('u123', '$2a$10$8Huwx9kBs5q78Hp9bDbVS.uOEfT0xy7V7OIImwuvRN5F0W4HWWgi.');

INSERT INTO users (username, password_hash, role) VALUES
('22127380', '$2a$10$QU2.4HZQ9X8Tx58rg5ZEs.v32gKbTJIn2cEs.Rl/Sgo.wuaaXl1Yu', "PREMIUM"),
('22127467', '$2a$10$ooXwxD/ZPIXznMzw1COsV.6K2QaLp/hVSPJ23oa8k845VTX4pAPhW', "ADMIN");

-- Bảng category (Lưu danh sách nhóm công cụ)
CREATE TABLE categories (
    id MEDIUMINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
) ENGINE=InnoDB;

INSERT INTO categories (name, description) VALUES
('Crypto', 'Tools related to encryption and data security'),
('Converter', 'Convert between different data formats'),
('Web', 'Tools for web development and optimization'),
('Images & Videos', 'Processing and manipulation of images and videos'),
('Development', 'Tools for programmers and developers'),
('Network', 'Network testing and analysis utilities'),
('Math', 'Mathematical and computational tools'),
('Measurement', 'Tools for various types of measurement'),
('Text', 'Text processing and manipulation tools'),
('Data', 'Tools for handling and encrypting data');

-- Bảng tools (Lưu danh sách công cụ)
CREATE TABLE tools (
    id MEDIUMINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    category_id MEDIUMINT NOT NULL,
    endpoint VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    is_premium BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO tools (name, description, endpoint, category_id) VALUES
-- Crypto (category_id = 1)
('Token Generator', 'Generate random string with the chars you want, uppercase or lowercase letters, numbers and/or symbols.', '/crypto/token-generator', 1),
('Hash Text', 'Hash a text string using the function you need : MD5, SHA1, SHA256, SHA224, SHA512, SHA384, SHA3 or RIPEMD160.', '/crypto/hash-text', 1),
('Bcrypt', 'Hash and compare text string using bcrypt. Bcrypt is a password-hashing function based on the Blowfish cipher.', '/crypto/bcrypt', 1),

-- Converter (category_id = 2)
('Text to NATO alphabet', 'Transform text into the NATO phonetic alphabet for oral transmission.', '/converter/text-to-nato-alphabet', 2),
('Text to ASCII binary', 'Convert text to its ASCII binary representation and vice-versa.', '/converter/text-to-ascii-binary', 2),
('Text to Unicode', 'Parse and convert text to unicode and vice-versa.', '/converter/text-to-unicode', 2),

-- Web (category_id = 3)
('URL Encode Decode', 'Encode text to URL-encoded format (also known as "percent-encoded"), or decode from it.', '/web/url-encode-decode', 3),
('Escape HTML Entities', 'Escape or unescape HTML entities (replace characters like <,>, &, " and \' with their HTML version).', '/web/escape-html-entities', 3),
('URL Parser', 'Parse a URL into its separate constituent parts (protocol, origin, params, port, username-password, ...).', '/web/url-parser', 3),
('Keycode Infor', 'Find the javascript keycode, code, location and modifiers of any pressed key.','/web/keycode-infor','3'),
('Slugify string', 'Make a string url, filename and id safe.', '/web/slugify-string',3),

-- Images & Videos (category_id = 4)
('QR Code Generator', 'Generate and download a QR code for a URL (or just plain text), and customize the background and foreground colors.', '/images-videos/qr-code-generator', 4),
('WiFi QR Code Generator', 'Generate and download QR codes for quick connections to WiFi networks.', '/images-videos/wifi-qr-code-generator', 4),
('SVG Placeholder Generator', 'Generate svg images to use as a placeholder in your applications.', '/images-videos/svg-placeholder-generator', 4),

-- Development (category_id = 5)
('Git Cheatsheet', 'Git is a decentralized version management software. With this cheatsheet, you will have quick access to the most common git commands.', '/development/git-cheatsheet', 5),
('Random Port Generator', 'Generate random port numbers outside of the range of "known" ports (0-1023).', '/development/random-port-generator', 5),
('JSON Prettify and Format', 'Prettify your JSON string into a friendly, human-readable format.', '/development/json-prettify-and-format', 5),

-- Network (category_id = 6)
('IPv4 Subnet Calculator', 'Parse your IPv4 CIDR blocks and get all the info you need about your subnet.', '/network/ipv4-subnet-calculator', 6),
('IPv4 Address Converter', 'Convert an IP address into decimal, binary, hexadecimal, or even an IPv6 representation of it.', '/network/ipv4-address-converter', 6),
('IPv4 Range Expander', 'Given a start and an end IPv4 address, this tool calculates a valid IPv4 subnet along with its CIDR notation.', '/network/ipv4-range-expander', 6),
('MAC Address Generator', 'Enter the quantity and prefix. MAC addresses will be generated in your chosen case (uppercase or lowercase)', '/network/mac-address-generator', 6),

-- Math (category_id = 7)
('Math Evaluator', 'A calculator for evaluating mathematical expressions. You can use functions like sqrt, cos, sin, abs, etc.', '/math/math-evaluator', 7),
('ETA Calculator', 'An ETA (Estimated Time of Arrival) calculator to determine the approximate end time of a task, for example, the end time and duration of a file download. With a concrete example, if you wash 5 plates in 3 minutes and you have 500 plates to wash, it will take you 5 hours to wash them all.', '/math/eta-calculator', 7),
('Percentage Calculator', 'Easily calculate percentages from a value to another value, or from a percentage to a value.', '/math/percentage-calculator', 7),

-- Measurement (category_id = 8)
('Chronometer', 'Monitor the duration of a thing. Basically a chronometer with simple chronometer features.', '/measurement/chronometer', 8),
('Temperature Converter', 'Degrees temperature conversions for Kelvin, Celsius, Fahrenheit, Rankine, Delisle, Newton, Réaumur, and Rømer.', '/measurement/temperature-converter', 8),

-- Text (category_id = 9)
('Text Statistics', 'Get information about a text, the number of characters, the number of words, its size in bytes, ...', '/text/text-statistics', 9),
('Numeronym Generator', 'A numeronym is a word where a number is used to form an abbreviation. For example, "i18n" is a numeronym of "internationalization" where 18 stands for the number of letters between the first i and the last n in the word.', '/text/numeronym-generator', 9),
('String obfuscator', 'Obfuscate a string (like a secret, an IBAN, or a token) to make it shareable and identifiable without revealing its content.', '/text/string-obfuscator', 9),

-- Data (category_id = 10)
('IBAN Validator and Parser', 'Validate and parse IBAN numbers. Check if an IBAN is valid and get the country, BBAN, if it is a QR-IBAN and the IBAN friendly format.', '/data/iban-validato-and-parser', 10);

-- Bảng favorites (Lưu danh sách công cụ yêu thích của user)
CREATE TABLE favorites (
    id MEDIUMINT PRIMARY KEY AUTO_INCREMENT,
    user_id MEDIUMINT NOT NULL,
    tool_id MEDIUMINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tool_id) REFERENCES tools(id) ON DELETE CASCADE
) ENGINE=InnoDB;

