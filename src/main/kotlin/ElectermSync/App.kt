package main.kotlin.ElectermSync

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import spark.Spark.*
import java.nio.charset.StandardCharsets
import java.util.*

class JwtException(message: String) : RuntimeException(message)

fun main() {
    val dotenv = Config()
    val secretOri = dotenv.getValue("JWT_SECRET")
    val bytesToEncode = secretOri.toByteArray(StandardCharsets.UTF_8)

    // Encode the bytes using Base64
    val secret = Base64.getEncoder().encodeToString(bytesToEncode)

    val ids = dotenv.getValue("JWT_USERS")
    val idArrStrings = ids.split(",").toTypedArray()
    Jwts.parserBuilder().setSigningKey(secret).build()

    port(Integer.parseInt((dotenv.getValue("PORT"))))

    ipAddress(dotenv.getValue("HOST"))

    before("/api/sync") { req, _ ->
        val authHeader = req.headers("Authorization") ?: ""
        try {
            if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
                throw JwtException("Missing or invalid token")
            } else {
                val token = authHeader.substring(7)
                val claimsJws: Jws<Claims> = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token)
                val id = claimsJws.body["id"].toString()
                val found = idArrStrings.any { element -> element == id }
                if (!found) {
                    throw JwtException("Unauthorized access")
                }
                req.attribute("jwtId", id)
            }
        } catch (ex: JwtException) {
            halt(401, "Unauthorized: ${ex.message}")
        }
    }

    get("/api/sync") { req, res ->
        val jwtId = req.attribute<Any>("jwtId").toString()
        val r = FileStore.read(jwtId, dotenv)
        res.status(r.statusCode)
        r.fileData
    }
    post("/api/sync") { req, res ->
        "test ok"
    }
    put("/api/sync") { req, res ->
        val requestBody = req.body()
        val jwtId = req.attribute<Any>("jwtId").toString()
        res.type("application/json")
        val r = FileStore.write(requestBody, jwtId, dotenv)
        res.status(r.statusCode)
        r.message
    }
    
    after("*") { _, res ->
        res.type("application/json")
        res.header("Content-Encoding", "gzip")
    }
}
