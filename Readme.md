# File Lister Spring Boot Applikáció

## Install

A `setup.sh` file futtatásával lehet az applickációt indítani. 

## Endpointok

- `/getUnique`: Kilistázza az összes alkönyvtárban található filenevét.
- `/history`: History-t adja vissza ki kérdezete le legutoljára.
- `/doc`: Dinamikusan generál javadoc oldalt és visszaadja azt.
- `/doc/download`: A javadoc mappát adja vissza letölthető formában zippelve.
- `/generate`: Egy minta JSON alapján generál file struktúrát teszthez.

---

## REST API paraméterek

### `GET /getUnique`

**Paraméterek:**
- `folder` (kötelező): A fő path ahonnan bejárja a könyvtáarakat
- `extension` (opcionális): Fájl kiterjesztés filter (e.g. `.txt`)

### `GET /history`

**Paraméterek:**
- `who`, `when`, `what` (opcionális filterek)

### ✅ `GET /doc`

--

### ✅ `GET /doc/download`

--

### ✅ `POST /generate`

**Példa minta:**
```json
{
  "mappa1": {
    "mappa2": {
      "fájl1": "egy.txt"
    }
  },
  "mappa2": {
    "fájl2": "kettő.txt"
  }
}
```
---
**Swagger UI: http://localhost:8080/swagger-ui/index.html**

