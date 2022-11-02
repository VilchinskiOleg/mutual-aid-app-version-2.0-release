package org.tms.common.auth.configuration.global.skip_list_impl;

import lombok.Getter;
import lombok.Setter;

public class SkipListMapImpl<K extends Comparable<K>, V> implements SkipListMap<K, V> {

  private Index root;
  private int capacity;

  @Override
  public V get(K key) {
    if (root == null || root.getNode() == null) {
      return null;
    } else if (root.getNode().getKey().compareTo(key) == 0) {
      return root.getNode().getValue();
    }

    Index next = root;
    Entry<K, V> res = null;

    while (next != null && res == null) {
      var rIndex = next.getRight();
      K nKey = null;

      if (rIndex != null && (nKey = rIndex.getNode().getKey()).compareTo(key) < 0) {
        next = rIndex;
      } else if (nKey != null && nKey.compareTo(key) == 0) {
        res = rIndex.getNode();
      } else {
        next = next.getDown();
      }
    };

    return res == null ? null : res.getValue();
  }

  @Override
  public void put(K key, V value) {

    if (root == null) {
      root = new Index(new Entry<>(key, value));
      return;
    }
    Index next = root;

    while (true) {
      var rIndex = next.getRight();
      K rKey = null;

      if (rIndex != null && (rKey = rIndex.getNode().getKey()).compareTo(key) < 0) {
        next = rIndex;
      } else if (rKey != null && rKey.compareTo(key) == 0) {
        next = rIndex;
        do {
          next.getNode().setValue(value);
          next = next.getDown();
        } while (next != null);
        break;
      } else {
        if (next.getDown() == null) {
          var nEntry = new Entry<>(key, value);
          var nIndex = new Index(nEntry);

          var prevRightInd = next.getRight();
          next.setRight(nIndex);
          nIndex.setRight(prevRightInd);
          break;
        } else {
          next = next.getDown();
        }
      }
    }
  }

  private void rebalance() {

  }

  @Getter
  private class Index{

    @Setter
    private Entry<K, V> node;
    @Setter
    private Index right;
    private Index down;

    public Index(Entry<K, V> node) {
      this.node = node;
    }
  }

  @Getter
  private class Entry<K, V>{

    private K key;
    @Setter
    private V value;

    @Setter
    private Entry<K, V> next;

    public Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}