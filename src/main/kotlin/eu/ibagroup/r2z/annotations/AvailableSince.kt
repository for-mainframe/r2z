package eu.ibagroup.r2z.annotations

import java.lang.annotation.Inherited

private const val osVersionPrefix = "z/OS"

enum class ZVersion(val version: String) {
  ZOS_2_1("$osVersionPrefix 2.1"),
  ZOS_2_2("$osVersionPrefix 2.2"),
  ZOS_2_3("$osVersionPrefix 2.3"),
  ZOS_2_4("$osVersionPrefix 2.4")
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Inherited
annotation class AvailableSince(val version: ZVersion)
