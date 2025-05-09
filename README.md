# Aspose Reproductions
## Words Table Issue
### Problem
- When running the included file (`testFiles/table.docx`), the outputted file builds the table but has shifted cells around, with some empty rows being deleted and cells being built incorrectly.

### Running the Reproduction
The application can be run as a Spring Boot application. Using an unlicensed version of Aspose will shift the output cells further, so values for `ASPOSE_PRIVATE_KEY` and `ASPOSE_PUBLIC_KEY` should be set as environment variables (or inserted into the properties file).

We have an endpoint which will fetch the problematic file and convert it. The output will be saved to `/pdfs`.

The endpoint is `http://localhost:8080/docx`.