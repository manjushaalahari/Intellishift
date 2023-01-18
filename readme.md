## Project
This is an automation testing project with the following objectives to test and validate - 
    1. Search for 'gel pens' on an e-commerce website
	2. Choose the last options from the suggestions
	3. Identify the lowest priced gel pens
	4. Add 2 units to the cart
	5. Empty the cart
	6. Validate the message on the web application

## Libraries used
Selenium Webdriver v4.7.2, 
Test NG v7.3.0,
Extent Reports v5.0.4,
Apache Maven v3.8.7

## Run guide
mvn clean test -Dtestng.dtd.http=true -Dbrowser=chrome

mvn clean test -Dtestng.dtd.http=true -Dbrowser=chrome_headless

##Test Results
Test results are captured in the index.html page of the project


