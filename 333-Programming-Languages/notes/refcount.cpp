/*
 * Reference counting smart pointer implementation.
 */

#include <iostream>
#include <string>
using namespace std;

template <class T> class SmartPointer
{
private:
	T* ptr;
	int* count;
	
public:
	SmartPointer(T* newptr)
	{
		this->ptr = newptr;
		this->count = new int(0);
		this->increment();
	}
	
	~SmartPointer()
	{
		this->decrement();
	}
	
	void print()
	{
		cout << "SmartPointer(" << ptr << "): " << *count << endl;
	}
	
	void increment()
	{
		(*this->count)++;
		this->print();
	}
	
	void decrement()
	{
		(*this->count)--;
		this->print();
		if (*this->count == 0)
		{
			cout << "SmartPointer(" << ptr << "): deleting reference." << endl;
			delete(ptr);
			free(count);
		}
	}
	
	SmartPointer(const SmartPointer& other)
	{
		this->ptr = other.ptr;
		this->count = other.count;
		this->increment();
	}
	
	SmartPointer<T>& operator=(const SmartPointer<T>& other)
	{
		if (this != &other)
		{
			// decrement old reference
			this->decrement();
			
			// update new reference
			this->ptr = other.ptr;
			this->count = other.count;
			this->increment();
		}
		return *this;
	}
	
	T& operator*()
	{ return *this->ptr; }
	
	T* operator->()
	{ return this->ptr; }
};

void pointerV4(SmartPointer<string> aptr)
{
	cout << *aptr << "," << aptr->length() << endl;
}

SmartPointer<string> pointerV5()
{
	SmartPointer<string> newptr(new string("return"));
	return newptr;
}

int main(int argc, char** argv)
{
	cout << "create..." << endl;
	SmartPointer<string> sp1(new string("hello"));
	SmartPointer<string> sp2(new string("world"));
	SmartPointer<string> sp3(sp1);
	
	cout << "pass by value..." << endl;
	pointerV4(sp1);
	SmartPointer<string> sp4 = pointerV5();
	
	cout << "assign..." << endl;
	sp1 = sp1;	
	sp2 = sp1;
	
	cout << "dereference..." << endl;
	cout << *sp1 << ", " << sp1->length() << endl;
	
	cout << "destroy..." << endl;
}