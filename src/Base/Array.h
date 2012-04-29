#pragma once

#include <iostream>

#include "Macros.h"

namespace magpie
{

  // A resizable dynamic array class. Array items must support copying and a
  // default constructor.
  template <class T>
  class Array
  {
  public:
    Array()
    : count_(0),
      capacity_(0),
      items_(NULL) {}

    Array(int capacity)
    : count_(0),
      capacity_(0),
      items_(NULL)
    {
      ensureCapacity_(capacity);
    }

    Array(int size, const T & fillWith)
    : count_(0),
      capacity_(0),
      items_(NULL)
    {
      ensureCapacity_(size);

      for (int i = 0; i < size; i++) items_[i] = fillWith;
      count_ = size;
    }

    Array(const Array<T> & array)
    : count_(0),
      capacity_(0),
      items_(NULL)
    {
      add(array);
    }

    ~Array()
    {
      clear();
    }

    // Gets the number of items currently in the array.
    int count() const { return count_; }

    // Gets whether or not the array is empty.
    bool isEmpty() const { return count_ == 0; }

    // Gets the maximum number of array the stack can hold before a
    // reallocation will occur.
    int capacity() const { return capacity_; }

    // Adds the given item to the end of the array, increasing its size
    // automatically.
    void add(const T & value)
    {
      ensureCapacity_(count_ + 1);
      items_[count_++] = value;
    }

    // Adds all of the items from the given array to this one.
    void add(const Array<T> & array)
    {
      ensureCapacity_(count_ + array.count_);

      for (int i = 0; i < array.count_; i++) items_[count_++] = array[i];
    }

    // Removes all items from the array.
    void clear()
    {
      if (items_ != NULL) delete [] items_;
      count_ = 0;
      capacity_ = 0;
    }

    // Removes the item at the given index. Indexes are zero-based from the
    // beginning of the array. Negative indexes are from the end of the array
    // and go forward, so that -1 is the last item in the array.
    void remove(int index)
    {
      if (index < 0) index = count_ + index;
      ASSERT_INDEX(index, count_);

      // Shift items up.
      for (int i = index; i < count_ - 1; i++) items_[i] = items_[i + 1];

      // Clear the copy of the last item.
      items_[count_ - 1] = T();
      count_--;
    }
    
    // Finds the index of the given item in the array. Returns -1 if not found.
    int indexOf(const T & value) const
    {
      for (int i = 0; i < count_; i++)
      {
        if (items_[i] == value) return i;
      }

      return -1;
    }
    
    // Assigns the contents of the given array to this one. Clears this array
    // and refills it with the contents of the other.
    Array & operator=(const Array & other)
    {
      // Early out of self-assignment.
      if (&other == this) return *this;

      clear();
      add(other);

      return *this;
    }

    // Gets the item at the given index. Indexes are zero-based from the
    // beginning of the array. Negative indexes are from the end of the array
    // and go forward, so that -1 is the last item in the array.
    T & operator[] (int index)
    {
      if (index < 0) index = count_ + index;
      ASSERT_INDEX(index, count_);

      return items_[index];
    }

    // Gets the item at the given index. Indexes are zero-based from the
    // beginning of the array. Negative indexes are from the end of the array
    // and go forward, so that -1 is the last item in the array.
    const T & operator[] (int index) const
    {
      if (index < 0) index = count_ + index;
      ASSERT_INDEX(index, count_);

      return items_[index];
    }

    // Reverses the order of the items in the array.
    void reverse()
    {
      for (int i = 0; i < count_ / 2; i++)
      {
        T temp = items_[i];
        items_[i] = items_[count_ - i - 1];
        items_[count_ - i - 1] = temp;
      }
    }

  private:
    void ensureCapacity_(int desiredCapacity)
    {
      // Early out if we have enough capacity.
      if (capacity_ >= desiredCapacity) return;

      // Figure out the new array size.
      // Instead of growing to just the capacity we need, we'll grow by a
      // multiple of the current size. This ensures amortized O(n) complexity
      // on adding instead of O(n^2).
      int capacity = capacity_;
      if (capacity < MIN_CAPACITY) capacity = MIN_CAPACITY;

      while (capacity < desiredCapacity) capacity *= GROW_FACTOR;

      // Create the new array.
      // TODO(bob): Should use the GC heap here, at least for some arrays.
      T* newItems = new T[capacity];

      // Copy the items over.
      for (int i = 0; i < count_; i++) newItems[i] = items_[i];

      // Delete the old one.
      delete [] items_;
      items_ = newItems;

      capacity_ = capacity;
    }

    static const int MIN_CAPACITY = 16;
    static const int GROW_FACTOR  = 2;

    int count_;
    int capacity_;
    T*  items_;
  };

}