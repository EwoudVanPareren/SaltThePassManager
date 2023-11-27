package nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass.ext

import org.bouncycastle.util.encoders.UrlBase64

internal fun ByteArray.toUrlBase64String() = UrlBase64.encode(this).toString(Charsets.UTF_8)