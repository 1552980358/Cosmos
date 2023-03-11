package projekt.cloud.piece.cosmos

/**
 * [ByteArrayCosmos]
 * A interface as a wrapper for Cosmos further putting into [android.os.Bundle]
 *
 * You may do something like implementing [ByteArrayCosmos] at a class, and as a wrapper class of
 * a data class, with `Kotlin Serialization`, `Gson` or [Serializable] of Java;
 * Or just store your data by your own method, and convert into [ByteArray].
 *
 * For example,
 * In Kotlin, with Kotlin Serialization
 * ```Kotlin
 *     class DataWrapper: ByteArrayCosmos {
 *
 *         @Serializable
 *         data class Data(
 *             @SerialName("title")
 *             var title: String
 *         )
 *
 *         private var _data: Data? = null
 *         val data: Data
 *             get() = _data!!
 *
 *         fun setData(data: Data) {
 *             _data = data
 *         }
 *
 *         override fun saveData(): ByteArray {
 *             return Json.encodeToString(data)
 *                 .encodeToByteArray()
 *         }
 *
 *         override fun recoverData(byteArray: ByteArray) {
 *             _data = Json.decodeFromString<Data>(String(byteArray))
 *         }
 *
 *     }
 * ```
 * In Java
 * ```Java
 *     public class DataWrapper implements ByteArrayCosmos {
 *
 *         static class Data implements Serializable {
 *
 *             private String title;
 *
 *             public void setTitle(String title) {
 *                 this.title = title;
 *             }
 *
 *             public String getTitle() {
 *                 return title;
 *             }
 *
 *         }
 *
 *         private Data data;
 *
 *         public void setData(Data data) {
 *             this.data = data;
 *         }
 *         public Data getData() {
 *             return data;
 *         }
 *
 *         @NotNull
 *         @Override
 *         public byte[] saveData() {
 *             byte[] byteArray;
 *
 *             try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
 *                 try (var objectOutputStream = new ObjectOutputStream(null)) {
 *                     objectOutputStream.writeObject(data);
 *                     byteArray = byteArrayOutputStream.toByteArray();
 *                 }
 *             } catch (IOException e) {
 *                 e.printStackTrace();
 *                 byteArray = new byte[0];
 *             }
 *
 *             return byteArray;
 *         }
 *
 *         @Override
 *         public void recoverData(@NotNull byte[] byteArray) {
 *             try (var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray))) {
 *                 data = (Data) objectInputStream.readObject();
 *             } catch (IOException | ClassNotFoundException e) {
 *                 e.printStackTrace();
 *             }
 *         }
 *
 *     }
 * ```
 *
 **/
interface ByteArrayCosmos {

    /**
     * [saveData]
     * To save data into a [ByteArray]
     * @return [ByteArray]
     **/
    fun saveData(): ByteArray

    /**
     * [recoverData]
     * To recover data from [ByteArray]
     * @param byteArray
     **/
    fun recoverData(byteArray: ByteArray)

}