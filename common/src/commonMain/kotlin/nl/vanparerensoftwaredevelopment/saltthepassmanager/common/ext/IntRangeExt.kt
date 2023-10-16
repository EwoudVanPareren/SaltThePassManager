package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ext

infix fun IntRange.containsEntirely(other: IntRange) =
    !other.isEmpty() && !isEmpty() &&
    first <= other.first && last >= other.last

infix fun IntRange.isEntirelyIn(other: IntRange) =
    other.containsEntirely(this)