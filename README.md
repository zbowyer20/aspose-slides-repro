# Aspose Reproductions
## Missing Images Issue
### Problem
- Images are sometimes missing in the converted PDF files when using Aspose for PPTX to PDF conversion.
- This issue seems to be dependent on application state or how it is running - as I've not been able to reproduce it when running as a local Java application, only when the application is running on our Kubernetes clusters
- Possibly related to available fonts on the machine?
- The warning we get when it does happen is: `Shape #4 at Slide #1 at position 66x113.8741 cannot be drawn`
- 
### Running the Reproduction
We containerise the application to try to get closer to the environment where the issue occurs.

From the root:
- `mvn clean package`
- `docker-compose up --build`

The endpoint can be hit:
- `http://localhost:8080/pptx`

This will save the converted PDF to `/output/presentation.pdf`