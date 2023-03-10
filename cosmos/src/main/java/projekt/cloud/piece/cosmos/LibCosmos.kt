package projekt.cloud.piece.cosmos

import java.io.Closeable
import java.io.Serializable

internal class LibCosmos(): Serializable, Closeable {

    companion object {

        private const val LIC_COSMOS = "cosmos"

        init {
            System.loadLibrary(LIC_COSMOS)
        }

    }

    /** Internal methods **/
    private external fun putByteArray(byteArray: ByteArray): Boolean
    private external fun getByteArray(pointer: Long): ByteArray?
    private external fun release(pointer: Long)

    constructor(byteArray: ByteArray): this() {
        put(byteArray)
    }

    private var pointer: Long = 0

    fun put(byteArray: ByteArray): Boolean {
        return putByteArray(byteArray)
    }

    val byteArray: ByteArray
        get() {
            return byteArray()!!
        }

    fun byteArray(): ByteArray? {
        return getByteArray(pointer)
    }

    override fun close() {
        release(pointer)
    }

}