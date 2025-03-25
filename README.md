# Aspose Reproductions
## Font Substitution Issue
### Problem
- Fonts are sometimes substituted for defaults even though the font is available on the system and has been loaded using `FontsLoader.loadExternalFonts`. This is at least partially related to the Aspose cache used for fonts. `FontsLoader.clearCache()` will sometimes fix the issue. *Question*: Is this the only way to update the cache when the external font folder has been updated? No write expiry or TTL?
- Sometimes even when using `FontsLoader.clearCache()` and ensuring all fonts are available, fonts are substituted. This is not reproducible in a predictable way, but seems to be related to the number of fonts in the cache and we have reproduced it when the cache contains 30+ fonts.
- Sometimes in our warning messages we see that fonts are being substituted for themselves. For instance, `1 - Font will be substituted from Wingdings to {Wingdings,OpenSymbol}`. *Question*: Is this just confusion around the wording of the log message? Or is there a bug here? When Consolas is being substituted we don't see that in the bracket-enclosed list.
- Sometimes including some fonts in the file triggers an error around casting from one Aspose type to another. This recurs until specific fonts are removed from the file, but later on the same files can be converted without issue. I noticed this happening with `Farisi`, `Baghdad` and `Mishafi` fonts.

### Running the Reproduction
We containerise the application. This is necessary as Aspose uses a number of default directories to fetch fonts from. These often contain system default fonts. This set of directories can't be configured. Therefore we need to containerise to control the runtime environment as much as possible. Without doing this, different systems might have a different set of fonts they can substitute with.

From the root:
- `docker-compose up --build`

We have a number of endpoints which will:
- Fetch a pptx file
- Copy over the font used in the file from one directory (`fonts-external`) to another (`fonts`). This mocks our process of downloading fonts from an external service.
- Configure the FontsLoader to load the external fonts from the `fonts` directory.
- Run the conversion against that pptx file
- Save the converted file in `pdfs`.

The endpoints are named after the font used:
- `http://localhost:8080/pptx/consolas`
- `http://localhost:8080/pptx/wingdings`

We expect that both conversions should complete successfully and without substitutions, but the problems outlined above arise intermittently.

Additional fonts, files and endpoints can be added to scale out the problem.