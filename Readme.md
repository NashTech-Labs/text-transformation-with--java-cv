<h3>Text Transformation with JavaCV</h3>


Text Transformation with JavaCV is an Optical character recognition service which coupled with the power of JavaCV and Lagom framework.
The service takes image of any format with API endpoint which has typed, handwritten or printed text on it and return text in the format of string.

<h4> To set up JavaCV for system -> </h4>

Install Tesseract on MacOS `brew install tesseract-lang`

Install Tesseract on Linux `sudo apt install tesseract-ocr -y`

<h4> Step to Follow -> </h4>

- compile the project with `sbt clean compile`


- Start the service with sbt runAll


- Use `/api/upload` POST endpoint to upload image 

