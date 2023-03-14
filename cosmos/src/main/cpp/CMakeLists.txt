cmake_minimum_required(VERSION 3.22.1)

project("cosmos")

add_library(cosmos SHARED
        libcosmos.cpp
        lib_cosmos_util.h lib_cosmos_util.cpp
        cosmos.h cosmos.cpp
        bitmap.h bitmap.cpp
        )

find_library(log-lib log)

target_link_libraries(cosmos jnigraphics ${log-lib})
