package org.jeecg.modules.project.entity;

import lombok.Data;

@Data
public class KeyValue<K,V> {

    K key;
    V value;
}
