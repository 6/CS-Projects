#include <iostream>
using namespace std;

class BigObject
{
public:
	BigObject(int value)
	{ x = value; }
	
	int x;
	
	int getX() const
	{ return x; }
	
	// copy constructor
	BigObject (const BigObject& other)
	{
		cout << "BigObject being copied!" << endl;
		x = other.x;
	}
};

int sumV1(BigObject o1, BigObject o2)
{
	return o1.x + o2.x;
}

int sumV2(BigObject* o1, BigObject* o2)
{
	return o1->x + o2->x;
}

int sumV3(const BigObject& o1, const BigObject& o2)
{
	return o1.getX() + o2.getX();
}

inline int sumV4(int i, int j)
{
	return i + j;
}

int main(int argc, char** argv)
{
	BigObject big1(5);
	BigObject big2(10);
	
	cout << "sumV1: " << sumV1(big1, big2) << endl;
	
	cout << "sumV2: " << sumV2(&big1, &big2) << endl;
	
	cout << "sumV3: " << sumV3(big1, big2) << endl;
	
	cout << "sumV4: " << sumV4(big1.getX(), big2.getX()) << endl;
}