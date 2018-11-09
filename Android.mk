LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_STATIC_JAVA_LIBRARIES :=  android-support-v4
LOCAL_PACKAGE_NAME := HostpotAutoStart
LOCAL_DEX_PREOPT := false
LOCAL_PROGUARD_ENABLED := full
LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)
##############################################
include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := 
                                        android-support-v4:libs/android-support-v4.jar 
include $(BUILD_MULTI_PREBUILT)
