def main():
	nome = str()
	sobrenome = str()
	idade = int()
	diferenca = int(0)
	print("Digite o seu nome: ")
	nome = str(input()) 
	print("Digite o seu sobrenome: ")
	sobrenome = str(input()) 
	print("Digite a sua idade: ")
	idade = int(input()) 
	if  idade<18:
		print("MENOR DE IDADE\n")
		while idade<18:
			diferenca=diferenca+1
			idade=idade+1

		print("RETORNE EM TANTOS ANOS: ")
		print("%d" %diferenca)

	elif idade>=18:
		print("MAIOR DE IDADE")

	else :
		print("ERRO")



main()
