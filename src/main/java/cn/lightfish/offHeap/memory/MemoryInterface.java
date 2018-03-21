package cn.lightfish.offHeap.memory;

public class MemoryInterface {
    final IntAllocInterface heapInterface;
    final LongAllocInterface offHeapInterface;
    final boolean isOffHeap;

    public MemoryInterface(IntAllocInterface memoryInterface) {
        this.heapInterface = memoryInterface;
        isOffHeap = false;
        this.offHeapInterface = null;
    }

    public MemoryInterface(LongAllocInterface offHeapInterface) {
        this.offHeapInterface = offHeapInterface;
        isOffHeap = true;
        this.heapInterface = null;
    }

    public MemoryInterface(IntAllocInterface heapInterface, LongAllocInterface offHeapInterface, boolean isOffHeap) {
        this.heapInterface = heapInterface;
        this.offHeapInterface = offHeapInterface;
        this.isOffHeap = isOffHeap;
    }

    public long allocateMemory(long size) {
        return isOffHeap ?
                offHeapInterface.allocateMemory(size) :
                heapInterface.allocateMemory((int) size);
    }

    public long reallocateMemory(long address, long bytes) {
        return isOffHeap ?
                offHeapInterface.reallocateMemory(address, bytes) :
                heapInterface.reallocateMemory((int) address, (int) bytes);
    }

    public void freeMemory(long address) {
        if (isOffHeap) {
            offHeapInterface.freeMemory(address);
        } else {
            heapInterface.freeMemory((int) address);
        }
    }

    public int getInt(long address) {
        if (isOffHeap) {
            return offHeapInterface.getInt(address);
        } else {
            return heapInterface.getInt((int) address);
        }
    }

    public void putInt(long address, int value) {
        if (isOffHeap) {
            offHeapInterface.putInt(address, value);
        } else {
            heapInterface.putInt((int) address, value);
        }
    }

    public byte getByte(long address) {
        if (isOffHeap) {
            return offHeapInterface.getByte(address);
        } else {
            return heapInterface.getByte((int) address);
        }
    }

    public void putByte(long address, byte value) {
        if (isOffHeap) {
            offHeapInterface.putByte(address, value);
        } else {
            heapInterface.putByte((int) address, value);
        }
    }

    public short getShort(long address) {
        if (isOffHeap) {
            return offHeapInterface.getShort(address);
        } else {
            return heapInterface.getShort((int) address);
        }
    }

    public void putShort(long address, short value) {
        if (isOffHeap) {
            offHeapInterface.putShort(address, value);
        } else {
            heapInterface.putShort((int) address, value);
        }
    }

    public long getLong(long address) {
        if (isOffHeap) {
            return offHeapInterface.getLong(address);
        } else {
            return heapInterface.getLong((int) address);
        }
    }

    public void putLong(long address, long value) {
        if (isOffHeap) {
            offHeapInterface.putLong(address, value);
        } else {
            heapInterface.putLong((int) address, value);
        }
    }

    public float getFloat(long address) {
        if (isOffHeap) {
            return offHeapInterface.getFloat(address);
        } else {
            return heapInterface.getFloat((int) address);
        }
    }

    public void putFloat(long address, float value) {
        if (isOffHeap) {
            offHeapInterface.putFloat(address, value);
        } else {
            heapInterface.putFloat((int) address, value);
        }
    }

    public double getDouble(long address) {
        if (isOffHeap) {
            return offHeapInterface.getDouble(address);
        } else {
            return heapInterface.getDouble((int) address);
        }
    }

    public void putDouble(long address, double value) {
        if (isOffHeap) {
            offHeapInterface.putDouble(address, value);
        } else {
            heapInterface.putDouble((int) address, value);
        }
    }

    public boolean getBoolean(long address) {
        if (isOffHeap) {
            return offHeapInterface.getBoolean(address);
        } else {
            return heapInterface.getBoolean((int) address);
        }

    }

    public void putBoolean(long address, boolean value) {
        if (isOffHeap) {
            offHeapInterface.putBoolean(address, value);
        } else {
            heapInterface.putBoolean((int) address, value);
        }
    }

    public char getChar(long address) {
        if (isOffHeap) {
            return offHeapInterface.getChar(address);
        } else {
            return heapInterface.getChar((int) address);
        }
    }

    public void putChar(long address, char value) {
        if (isOffHeap) {
            offHeapInterface.putChar(address, value);
        } else {
            heapInterface.putChar((int) address, value);
        }
    }

    public long getAddress(long address) {
        if (isOffHeap) {
            return offHeapInterface.getAddress(address);
        } else {
            return heapInterface.getAddress((int) address);
        }
    }

    public void putAddress(long address, long value) {
        if (isOffHeap) {
            offHeapInterface.putAddress(address, value);
        } else {
            heapInterface.putAddress(Long.valueOf(address).intValue(), Long.valueOf(value).intValue());
        }
    }

    public void setMemory(long address, long bytes, byte value) {
        if (isOffHeap) {
            offHeapInterface.setMemory(address, bytes, value);
        } else {
            heapInterface.setMemory((int) address, (int) bytes, value);
        }
    }

    public void copyMemory(long srcAddress, long destAddress, int bytes) {
        if (isOffHeap) {
            offHeapInterface.copyMemory(srcAddress, destAddress, bytes);
        } else {
            heapInterface.copyMemory((int) srcAddress, (int) destAddress, bytes);
        }
    }

    public boolean isOffHeap() {
        return isOffHeap;
    }
}
