package com.palomamobile.android.sdk.media;

import retrofit.RetrofitError;

/**
 * Created by Karel Herink
 */
public interface IChunkingStrategy {
    boolean isApplyChunking();

    int getChunkSize();

    void adjustStrategyAfterSuccess();

    void adjustStrategyAfterFailure(Throwable throwable);

    class SimpleChunkingStrategy implements IChunkingStrategy {

        private final static int MAXIMUM_CHUNK_SIZE = 32 * 1024;
        private final static int INITIAL_CHUNK_SIZE = MAXIMUM_CHUNK_SIZE;
        private final static int MINIMUM_CHUNK_SIZE = 512;

        private int chunkSize = -1;

        public SimpleChunkingStrategy() {}

        public SimpleChunkingStrategy(int chunkSize) {
            this.chunkSize = chunkSize;
        }

        @Override
        public boolean isApplyChunking() {
            return chunkSize > 0;
        }

        @Override
        public int getChunkSize() {
            return chunkSize;
        }

        @Override
        public void adjustStrategyAfterSuccess() {
            //no-op
        }

        @Override
        public void adjustStrategyAfterFailure(Throwable throwable) {
            if (shouldAdjustStrategyForFailure(throwable)) {
                if (!isApplyChunking()) {
                    chunkSize = INITIAL_CHUNK_SIZE;
                }
                int nextChunkSize = chunkSize / 2;
                chunkSize = (nextChunkSize >= MINIMUM_CHUNK_SIZE ? nextChunkSize : MINIMUM_CHUNK_SIZE);
            }
        }

        boolean shouldAdjustStrategyForFailure(Throwable throwable) {
            if (throwable instanceof RetrofitError) {
                RetrofitError retrofitError = (RetrofitError) throwable;
                return RetrofitError.Kind.NETWORK.equals(retrofitError.getKind());
            }
            return false;
        }

        void forceChunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
        }

        @Override
        public String toString() {
            return "SimpleChunkingStrategy{" +
                    "isApplyChunking=" + isApplyChunking() + ", " +
                    "chunkSize=" + chunkSize +
                    '}';
        }
    }
}
