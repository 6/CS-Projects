#include <iostream>
#include <string>
using namespace std;

template <class T> class AutoPtr
{
/*
 * An auto pointer implementation, similar to the 
 * stl auto_ptr. Some ideas provided by 
 * Scott Meyer (http://www.aristeia.com/BookErrata/auto_ptr-update.html)
 * and 
 * Yonat Sharon (http://ootips.org/yonat/4dev/smart-pointers.html)
 */

private:
	T* ptr;
public:
	// construct with a new object
	AutoPtr(T* newptr)
	{
		ptr = newptr;
		cout << "AutoPtr: construct" << endl;
	}
	
	// delete the pointer on destruction
	~AutoPtr()
	{
		delete(this->ptr);
		cout << "AutoPtr: destruct" << endl;
	}

	// * operator returns pointer
	T& operator*()
	{ return *ptr; }
	
	// -> operator returns object
	T* operator->()
	{ return ptr; }
	
	// assignment operator transfers ownership
	AutoPtr<T>& operator=(AutoPtr<T>& other)
	{
	  // no self assignment
		if (this != &other)
		{
			cout << "AutoPtr: assign => transfer ownership" << endl;
			delete(this->ptr);
			this->ptr = other.ptr;
			other.ptr = NULL; // clear addr for other ptr
		}
		else
		{
			cout << "AutoPtr: assign self => nothing to do" << endl;
		}
		return *this;
	}
	
	// copy constructor transfers ownership to deal w/ pass-by-value
	AutoPtr(AutoPtr<T>& other)
	{
		cout << "AutoPtr: copy construct => transfer ownership" << endl;
		this->ptr = other.ptr;
		other.ptr=NULL;
	}
};

class MyClass
{
private:
	string message;
public:
	MyClass(string msg)
	{ message = msg; cout << "MyClass: construct" << endl; }
	
	~MyClass()
	{ cout << "MyClass: destruct => " << message << endl; }
		
	MyClass(const MyClass& other)
	{ this->message = other.message; cout << "MyClass: copy construct" << endl; }

	MyClass& operator=(const MyClass& other)
	{ this->message = other.message; cout << "MyClass: assign" << endl; }
		
	void print()
	{	cout << "MyClass(" << this << "): " << message << endl; }
};

void pointerV1()
{
	MyClass* ptr = new MyClass("pointer reference"); // created on the heap
	ptr->print();
	delete(ptr);
}

void pointerV2()
{
	AutoPtr<MyClass> aptr1(new MyClass("auto 1")); // created on the stack
	aptr1->print();
	
	AutoPtr<MyClass> aptr2(new MyClass("auto 2"));
	aptr2 = aptr1;
	aptr2 = aptr2;
}

int main(int argc, char** argv)
{	
	pointerV1();
	pointerV2();	
}