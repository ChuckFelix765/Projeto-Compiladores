#include <stdio.h>

int main(){
int a;
int b;
int c;
float d;
printf("Programa Teste \n");
printf("Digite A: ");
scanf("%d", &a);
printf("Digite B: ");
scanf("%d", &b);
if (a<b){
c=a+b;

}else {
c=a-b;

}printf("C eh igual a: ");
printf("%d\n",c);
d=c/(a+b);
printf("D e igual a: ");
printf("%f\n",d);

}
