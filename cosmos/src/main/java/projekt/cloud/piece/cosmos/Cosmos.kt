package projekt.cloud.piece.cosmos

import android.os.Bundle
import projekt.cloud.piece.cosmos.LibCosmos.Companion.POINTER_NULL

object Cosmos {

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
     *     Cosmos.putCosmosToBundle(getBundle(), "cosmos_name" data);
     * ```
     **/
    @JvmName("putCosmosToBundle")
    @JvmStatic
    fun Bundle.putCosmos(name: String, byteArrayCosmos: ByteArrayCosmos): Boolean {
        putLong(
            name,
            LibCosmos().also { cosmos ->
                if (!cosmos.put(byteArrayCosmos.saveData())) {
                    return false
                }
            }.pointer
        )
        return true
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
            recoverData(getCosmos(name).byteArray)
        }
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
        return getCosmos(name).use { cosmos ->
            block.invoke().apply {
                recoverData(cosmos.byteArray)
            }
        }
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
        getCosmosNullable(name)?.close()
    }

    private fun Bundle.getCosmosPointer(name: String): Long {
        return getLong(name, POINTER_NULL)
    }

    private fun Bundle.getCosmosNullable(name: String): LibCosmos? {
        if (!isCosmosExists(name)) {
            return null
        }
        return LibCosmos(getCosmosPointer(name))
    }

    private fun Bundle.getCosmos(name: String): LibCosmos {
        return getCosmosNullable(name)!!
    }

}