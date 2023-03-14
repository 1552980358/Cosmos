package projekt.cloud.piece.cosmos

import android.graphics.Bitmap
import android.os.Bundle
import projekt.cloud.piece.cosmos.LibCosmos.Companion.LibCosmos
import projekt.cloud.piece.cosmos.LibCosmos.Companion.POINTER_NULL

object Cosmos {

    /**
     * Method [putCosmos]; Java method name `putCosmosToBundle()`:
     * Put [ByteArray] into [Bundle]
     *
     * @param name
     * @param byteArray
     *
     * Usage:
     * In Kotlin
     * ```Kotlin
     *     import projekt.cloud.piece.cosmos.Cosmos.putCosmos
     *     ...
     *     getBundle().putCosmos("cosmos_name", byteArray)
     * ```
     * In Java
     * ```Java
     *     import projekt.cloud.piece.cosmos.Cosmos;
     *     ...
     *     var data = new Data();
     *     Cosmos.putCosmosToBundle(getBundle(), "cosmos_name", getByteArray());
     * ```
     **/
    @JvmName("putCosmosToBundle")
    fun Bundle.putCosmos(name: String, byteArray: ByteArray): Boolean {
        return LibCosmos(getLong(name, POINTER_NULL)) {
            if (put(byteArray)) {
                putLong(name, pointer)
            }
        }
    }

    /**
     * Consider a class called `Data`, which implements to interface [ByteArrayCosmos]
     *
     * In Kotlin
     * ```Kotlin
     *     import projekt.cloud.piece.cosmos.ByteArrayCosmos
     *
     *     class Data(): ByteArrayCosmos {
     *         override fun saveData(): ByteArray {
     *             val byteArray = ByteArray(getByteArraySize())
     *             // Do something like
     *             // copyDataIntoByteArray(byteArray)
     *             return byteArray
     *         }
     *         override fun recoverData(byteArray: ByteArray) {
     *             // Do something like
     *             // recoverDataWithByteArray(byteArray)
     *         }
     *     }
     * ```
     *
     * In Java
     * ```Java
     *     import projekt.cloud.piece.cosmos.ByteArrayCosmos;
     *
     *     public class Example implements ByteArrayCosmos {
     *         @NotNull
     *         @Override
     *         public byte[] saveData() {
     *             var byteArray = new byte[getByteArraySize()];
     *             // Do something like
     *             // copyDataIntoByteArray(byteArray)
     *             return byteArray;
     *         }
     *         @Override
     *         public void recoverData(@NotNull byte[] byteArray) {
     *             // Do something like
     *             // recoverDataWithByteArray(byteArray)
     *         }
     *     }
     * ```
     **/

    /**
     * Method [putCosmos]; Java method name `putCosmosToBundle()`:
     * Put [ByteArrayCosmos] into [Bundle]
     *
     * @param name
     * @param byteArrayCosmos
     *
     * Usage:
     * In Kotlin
     * ```Kotlin
     *    import projekt.cloud.piece.cosmos.Cosmos.putCosmos
     *    ...
     *    val dat = Data()
     *    getBundle().putCosmos("cosmos_name", dat)
     * ```
     * In Java
     * ```Java
     *     import projekt.cloud.piece.cosmos.Cosmos;
     *     ...
     *     var data = new Data();
     *     Cosmos.putCosmosToBundle(getBundle(), "cosmos_name", data);
     * ```
     **/
    @JvmName("putCosmosToBundle")
    @JvmStatic
    fun Bundle.putCosmos(name: String, byteArrayCosmos: ByteArrayCosmos): Boolean {
        return putCosmos(name, byteArrayCosmos.saveData())
    }

    /**
     * Method [putCosmos]; Java method `putCosmosToBundle()`:
     * Put [Bitmap] into [Bundle]
     *
     * @param name [String]
     * @param bitmap [Bitmap]
     * @return [Boolean]
     *
     * In Kotlin
     * ```Kotlin
     *    import projekt.cloud.piece.cosmos.Cosmos.putCosmos
     *    ...
     *    val bitmap = getBitmap()
     *    getBundle().putCosmos("cosmos_name", bitmap)
     * ```
     * In Java
     * ```Java
     *     import projekt.cloud.piece.cosmos.Cosmos;
     *     ...
     *     var bitmap = getBitmap();
     *     Cosmos.putCosmosToBundle(getBundle(), "cosmos_name", bitmap);
     * ```
     **/
    @JvmName("putCosmosToBundle")
    @JvmStatic
    fun Bundle.putCosmos(name: String, bitmap: Bitmap): Boolean {
        return LibCosmos(getLong(name, POINTER_NULL)) {
            if (put(bitmap)) {
                putLong(name, pointer)
            }
        }
    }

    /**
     * Method [isCosmosExists]; Java method `Cosmos.checkIsCosmosExists()`:
     * Check if Cosmos is existed in Bundle
     *
     * @param name
     *
     * Usage:
     * In Kotlin
     * ```Kotlin
     *     import projekt.cloud.piece.cosmos.Cosmos.isCosmosExists
     *     ...
     *     val bundle = getBundle()
     *     if (bundle.isCosmosExists("cosmos_name")) {
     *         ...
     *     }
     * ```
     * In Java
     * ```Java
     *     import projekt.cloud.piece.cosmos.Cosmos;
     *     ...
     *     var bundle = getBundle();
     *     if (Cosmos.checkIsCosmosExists(bundle, "cosmos_name")) {
     *         ...
     *     }
     * ```
     **/
    @JvmName("checkIsCosmosExists")
    fun Bundle.isCosmosExists(name: String): Boolean {
        return containsKey(name) && getCosmosPointer(name) != POINTER_NULL
    }

    /**
     * Method [getCosmos]; Java method name `getCosmosFromBundle()`:
     * Get ByteArray from Bundle, and provide stored [ByteArray] to [T],
     * which implemented [ByteArrayCosmos].
     * You should call [clearCosmos] when you do not need [LibCosmos.pointer] pointed [ByteArray] in
     * memory anymore.
     * Other than self calling [clearCosmos], using [extractCosmos] is advised to get a copy and
     * release [LibCosmos.pointer] pointed memory space, letting JVM controls over the instance GC
     *
     * @param name
     * @param block
     *
     * Usage:
     * In Kotlin
     * ```Kotlin
     *     import projekt.cloud.piece.cosmos.Cosmos.getCosmos
     *     ...
     *     getBundle().getCosmos("cosmos_name") { Data() }
     * ```
     * In Java
     * ```
     *     import projekt.cloud.piece.cosmos.Cosmos;
     *     ...
     *     Cosmos.getCosmosFromBundle(getBundle(), "cosmos_name, Data::new);
     * ```
     **/
    @JvmName("getCosmosFromBundle")
    @JvmStatic
    fun <T: ByteArrayCosmos> Bundle.getCosmos(name: String, block: () -> T): T {
        return block.invoke().apply {
            recoverData(getCosmos(name))
        }
    }

    /**
     * Method [getCosmos]; Java method `getCosmosFromBundle()`:
     * Get [ByteArray] from [Bundle]
     * You should call [clearCosmos] when you do not need [LibCosmos.pointer] pointed [ByteArray] in
     * memory anymore.
     * Other than self calling [clearCosmos], using [extractCosmos] is advised to get a copy and
     * release [LibCosmos.pointer] pointed memory space, letting JVM controls over the instance GC
     *
     * @param name
     * @return [ByteArray]
     **/
    @JvmName("getCosmosFromBundle")
    fun Bundle.getCosmos(name: String): ByteArray {
        return getLibCosmos(name).byteArray
    }

    /**
     * Method [extractCosmos]; Java method name: `extractCosmosFromBundle()`;
     * Same function as [getCosmos], but will clear cached [ByteArray] content.
     * It is strongly advised to use this method to copy your own [ByteArray] instance,
     * other than storing [ByteArray] pointed by [LibCosmos.pointer],
     * which may lead to memory leaks if you forgot to release [LibCosmos.pointer] pointed [ByteArray]
     * in memory by calling [clearCosmos].
     * Getting a copy and release [LibCosmos.pointer] pointed memory space, and let JVM controls over
     * the instance GC will gain a better memory performance.
     *
     * @param name
     * @param block
     *
     * Usage:
     * In Kotlin
     * ```Kotlin
     *     import projekt.cloud.piece.cosmos.Cosmos.getCosmos
     *     ...
     *     getBundle().getCosmos("cosmos_name") { Data() }
     * ```
     * In Java
     * ```
     *     import projekt.cloud.piece.cosmos.Cosmos;
     *     ...
     *     Cosmos.getCosmosFromBundle(getBundle(), "cosmos_name, Data::new);
     * ```
     **/
    @JvmName("extractCosmosFromBundle")
    fun <T: ByteArrayCosmos> Bundle.extractCosmos(name: String, block: () -> T): T {
        return block.invoke()
            .apply { recoverData(extractCosmos(name)) }
    }

    /**
     * Method [extractCosmos]; Java method `extractCosmosFromBundle()`:
     * Same function as [getCosmos], but will clear cached [ByteArray] content.
     * It is strongly advised to use this method to copy your own [ByteArray] instance,
     * other than storing [ByteArray] pointed by [LibCosmos.pointer],
     * which may lead to memory leaks if you forgot to release [LibCosmos.pointer] pointed [ByteArray]
     * in memory by calling [clearCosmos].
     * Getting a copy and release [LibCosmos.pointer] pointed memory space, and let JVM controls over
     * the instance GC will gain a better memory performance.
     *
     * @param name
     * @return [ByteArray]
     **/
    @JvmName("extractCosmosFromBundle")
    fun Bundle.extractCosmos(name: String): ByteArray {
        return getLibCosmos(name)
            .use { it.byteArray }
    }

    /**
     * Method [clearCosmos]; Java method name `clearCosmosFromBundle()`:
     * Clear [LibCosmos.pointer] pointed memory, and release that
     *
     * Usage:
     * In Kotlin
     * ```Kotlin
     *     import projekt.cloud.piece.cosmos.Cosmos.clearCosmos
     *     ...
     *     getBundle().clearCosmos("cosmos_name")
     * ```
     * In Java
     * ```
     *     import projekt.cloud.piece.cosmos.Cosmos;
     *     ...
     *     Cosmos.clearCosmosFromBundle(getBundle(), "cosmos_name");
     * ```
     **/
    @JvmName("clearCosmosFromBundle")
    fun Bundle.clearCosmos(name: String) {
        getLibCosmosNullable(name)?.close()
    }

    private fun Bundle.getCosmosPointer(name: String): Long {
        return getLong(name, POINTER_NULL)
    }

    private fun Bundle.getLibCosmosNullable(name: String): LibCosmos? {
        if (!isCosmosExists(name)) {
            return null
        }
        return LibCosmos(getCosmosPointer(name))
    }

    private fun Bundle.getLibCosmos(name: String): LibCosmos {
        return getLibCosmosNullable(name)!!
    }

}