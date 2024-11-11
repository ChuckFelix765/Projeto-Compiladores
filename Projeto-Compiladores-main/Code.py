def main():
	a = int()
	b = int()
	c = int()
	d = float()
	print("Programa Teste \n")
	print("Digite A: ")
	a = int(input()) 
	print("Digite B: ")
	b = int(input()) 
	if  a<b:
		c=a+b

	else :
		c=a-b

	print("C eh igual a: ")
	print("%d" %c)
	d=c/(a+b)
	print("D e igual a: ")
	print("%f" %d)


main()
