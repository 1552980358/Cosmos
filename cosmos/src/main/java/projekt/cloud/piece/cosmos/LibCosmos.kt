package projekt.cloud.piece.cosmos

import java.io.Closeable
import java.io.Serializable

internal class LibCosmos @JvmOverloads constructor(
    internal var pointer: Long = POINTER_NULL
): Serializable, Closeable {

    companion object {

        private const val LIC_COSMOS = "cosmos"

        init {
            System.loadLibrary(LIC_COSMOS)
        }

        internal const val POINTER_NULL = 0L
    }

    /** Internal methods **/
    private external fun putByteArray(byteArray: ByteArray): Boolean
    private external fun getByteArray(pointer: Long): ByteArray?
    private external fun release(pointer: Long)

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