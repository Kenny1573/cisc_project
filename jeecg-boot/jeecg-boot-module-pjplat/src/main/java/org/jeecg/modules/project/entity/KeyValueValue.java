package org.jeecg.modules.project.entity;

import lombok.Data;

@Data
public class KeyValueValue<K,S,V> {
    K key;
    S value;
    V result;
}
