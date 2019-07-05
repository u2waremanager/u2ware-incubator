package io.github.u2ware.sample;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MultiValueMapBuilder {

    public static <A,B> Builder<A,B> add(A key, B value){
    	return new Builder<A,B>().add(key, value);
    }

    public static class Builder<K,V>{
        private MultiValueMap<K, V> map;

        private Builder(){
            this.map = new LinkedMultiValueMap<>();
        }

        public Builder<K,V> add(K key, V value){
            this.map.add(key, value);
            return this;
        }
        
        public MultiValueMap<K,V> build(){
            return this.map;
        }
    }
}
