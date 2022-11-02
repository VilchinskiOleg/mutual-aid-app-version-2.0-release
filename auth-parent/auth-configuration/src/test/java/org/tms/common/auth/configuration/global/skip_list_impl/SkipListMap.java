package org.tms.common.auth.configuration.global.skip_list_impl;

public interface SkipListMap<K extends Comparable<K>, V> {

  V get(K key);

  void put(K key, V value);
}