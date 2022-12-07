package org.tms.common.auth.configuration.global.core.skip_list_map_impl;

public interface SkipListMap<K extends Comparable<K>, V> {

  V get(K key);

  void put(K key, V value);
}