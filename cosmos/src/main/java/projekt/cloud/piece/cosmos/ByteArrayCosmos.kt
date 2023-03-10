package projekt.cloud.piece.cosmos

interface ByteArrayCosmos {

    fun saveData(): ByteArray

    fun recoverData(byteArray: ByteArray)

}