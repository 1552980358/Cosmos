package projekt.cloud.piece.cosmos

import android.graphics.Bitmap
import java.io.Closeable
import java.io.Serializable

internal class LibCosmos constructor(
    internal var pointer: Long = POINTER_NULL
): Serializable, Closeable {

    companion object {

        private const val LIC_COSMOS = "cosmos"

        init {
            System.loadLibrary(LIC_COSMOS)
        }

        internal const val POINTER_NULL = 0L

        @Suppress("FunctionName")
        internal fun LibCosmos(pointer: Long, block: LibCosmos.() -> Unit): Boolean {
            return LibCosmos(pointer).let {
                block.invoke(it)
                pointer != POINTER_NULL
            }
        }

    }

    /** Internal methods **/
    private external fun putByteArray(byteArray: ByteArray): Boolean
    private external fun putBitmap(bitmap: Bitmap): Boolean
    private external fun getByteArray(pointer: Long): ByteArray?
    private external fun getBitmap(pointer: Long): Bitmap?
    private external fun release(pointer: Long)

    fun put(byteArray: ByteArray): Boolean {
        return putByteArray(byteArray)
    }

    fun put(bitmap: Bitmap): Boolean {
        return putBitmap(bitmap)
    }

    val byteArray: ByteArray
        get() {
            return byteArray()!!
        }

    fun byteArray(): ByteArray? {
        return getByteArray(pointer)
    }

    val bitmap: Bitmap
        get() = bitmap()!!

    fun bitmap(): Bitmap? {
        return getBitmap(pointer)
    }

    override fun close() {
        release(pointer)
    }

}