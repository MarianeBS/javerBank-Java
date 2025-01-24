package com.msouza.requisition.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreUtils {
    public static Float calcularScore(float saldo) {
        return (float) (saldo*0.1);
    }

}
