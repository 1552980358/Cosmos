package projekt.cloud.piece.cosmos

import java.io.Serializable

class LibCosmos: Serializable {

    companion object {

        private const val LIC_COSMOS = "cosmos"

        init {
            System.loadLibrary(LIC_COSMOS)
        }

    }

    private var pointer: Long = 0

    private external fun putByteArray(byteArray: ByteArray): Boolean

    private external fun getByteArray(pointer: Long): ByteArray?

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

}