#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Reteta {
	unsigned int nr_reteta;
	unsigned char nr_medicamente;
	char** lista_medicamente;
	unsigned char* cantitati_medicamente;
	float* preturi_medicamente;
	unsigned char* procente_compensare_medicamente;
	char* nume_medic;
};

typedef struct Reteta Reteta;

struct nod {
	Reteta info;
	struct nod* next;
	struct nod* prev;
};

typedef struct nod Nod;

struct ListaDubla {
	Nod* prim;
	Nod* ultim;
};

typedef struct ListaDubla ListaDubla;

//inserare la sfarsit ex2
ListaDubla inserare_sfarsit(ListaDubla lista, Reteta reteta) {
	Nod* nou = (Nod*)malloc(sizeof(Nod));
	nou->info = reteta;
	nou->next = NULL;
	nou->prev = lista.ultim;
	if (lista.ultim != NULL) {
		lista.ultim->next = nou;
	}
	else {
		lista.prim = nou;
	}
	lista.ultim = nou;
	return lista;
}
//citire fisier ex2

ListaDubla citire_fisier(char* numeFisier) {
	FILE* f = fopen(numeFisier, "r");
	ListaDubla lista = { NULL, NULL };
	char buffer[128];

	while (fgets(buffer, sizeof(buffer), f)) {
		Reteta r;
		r.nr_reteta = (unsigned int)atoi(buffer);

		fgets(buffer, sizeof(buffer), f);
		buffer[strcspn(buffer, "\n")] = 0;
		r.nume_medic = _strdup(buffer);

		fgets(buffer, sizeof(buffer), f);
		r.nr_medicamente = (unsigned char)atoi(buffer);

		r.lista_medicamente = malloc(r.nr_medicamente * sizeof(char*));
		for (int i = 0; i < r.nr_medicamente; i++) {
			fgets(buffer, sizeof(buffer), f);
			buffer[strcspn(buffer, "\n")] = 0;
			r.lista_medicamente[i] = _strdup(buffer);
		}

		r.cantitati_medicamente = malloc(r.nr_medicamente);
		for (int i = 0; i < r.nr_medicamente; i++) {
			fgets(buffer, sizeof(buffer), f);
			r.cantitati_medicamente[i] = (unsigned char)atoi(buffer);
		}

		r.preturi_medicamente = malloc(r.nr_medicamente * sizeof(float));
		for (int i = 0; i < r.nr_medicamente; i++) {
			fgets(buffer, sizeof(buffer), f);
			r.preturi_medicamente[i] = (float)atof(buffer);
		}

		r.procente_compensare_medicamente = malloc(r.nr_medicamente);
		for (int i = 0; i < r.nr_medicamente; i++) {
			fgets(buffer, sizeof(buffer), f);
			r.procente_compensare_medicamente[i] = (unsigned char)atoi(buffer);
		}

		lista = inserare_sfarsit(lista, r);
	}

	fclose(f);
	return lista;
}


//functie pentru afisarea listei ex2
void traversare(ListaDubla lista)
{
	Nod* tmp = lista.prim;
	while (tmp)
	{
		printf("Reteta nr %u\n", tmp->info.nr_reteta);
		for (int i = 0; i < tmp->info.nr_medicamente; i++)
		{
			printf("Medicament %s, cantitate %hhu, pret %.2f, procent compensare %hhu\n", tmp->info.lista_medicamente[i], tmp->info.cantitati_medicamente[i], tmp->info.preturi_medicamente[i], tmp->info.procente_compensare_medicamente[i]);
		}
		tmp = tmp->next;
		printf("\n");
	}
}
//functie pentru calcularea incasarilor ex3
float calcul_incasari(ListaDubla lista, const char* medicament)
{
	float total = 0.0f;
	Nod* tmp = lista.prim;
	while (tmp)
	{
		for (int i = 0; i < tmp->info.nr_medicamente; i++)
		{
			if (strcmp(tmp->info.lista_medicamente[i], medicament) == 0)
			{
				total += tmp->info.preturi_medicamente[i] * tmp->info.cantitati_medicamente[i];
			}
		}
		tmp = tmp->next;
	}
	return total;
}

//functie pentru copierea intr un vector ex 4
Reteta* copiere_reteta_medic(ListaDubla lista, const char* nume_medic, int* nr_reteta)
{
	Reteta* vector = NULL;
	*nr_reteta = 0;
	Nod* tmp = lista.prim;
	while (tmp)
	{
		if (strcmp(tmp->info.nume_medic, nume_medic) == 0)
		{
			vector = realloc(vector, (*nr_reteta + 1) * sizeof(Reteta));
			vector[*nr_reteta] = tmp->info;
			(*nr_reteta)++;
		}
		tmp = tmp->next;
	}
	return vector;
}

//functie determinarea valorii compensate din vector ex5
float valoare_compensata(Reteta* reteta, int nr_reteta)
{
	float total = 0.0f;
	for (int i = 0; i < nr_reteta; i++)
	{
		for (int j = 0; j < reteta[i].nr_medicamente; j++)
		{
			total += (reteta[i].preturi_medicamente[j] * reteta[i].cantitati_medicamente[j] * reteta[i].procente_compensare_medicamente[j]) / 100;
		}
	}
	return total;
}
//functie pentru determinarea cantitatii vandute ex 6
unsigned int cantitate_vanduta(ListaDubla lista, const char* medicament)
{
	unsigned int total = 0;
	Nod* tmp = lista.prim;
	while (tmp)
	{
		for (int i = 0; i < tmp->info.nr_medicamente; i++)
		{
			if (strcmp(tmp->info.lista_medicamente[i], medicament) == 0)
			{
				total += tmp->info.cantitati_medicamente[i];
			}
		}
		tmp = tmp->next;
	}
	return total;
}
//functie dezalocare memorie ex7
void dezalocare(ListaDubla* lista)
{
	Nod* tmp = lista->prim;
	while (tmp)
	{
		Nod* urmator = tmp->next;
		free(tmp->info.nume_medic);
		for (int i = 0; i < tmp->info.nr_medicamente; i++)
		{
			free(tmp->info.lista_medicamente[i]);
		}
		free(tmp->info.lista_medicamente);
		free(tmp->info.cantitati_medicamente);
		free(tmp->info.preturi_medicamente);
		free(tmp->info.procente_compensare_medicamente);
		free(tmp);
		tmp = urmator;
	}
	lista->prim = lista->ultim = NULL;
}

int main()
{
	//validarea implemantarii ex 2
	ListaDubla lista = citire_fisier("Retete_medicale.txt");
	printf("Lista retetelor dupa citirea din fisier :\n\n");
	traversare(lista);

	//validarea implementarii ex 3
	char medicament[] = "Paracetamol";
	char medicament2[] = "Ibuprofen";
	float incasari = calcul_incasari(lista, medicament);
	printf("Incasarile totale pentru medicamentul %s sunt: %.2f\n", medicament, incasari);
	printf("Incasarile totale pentru medicamentul %s sunt: %.2f\n", medicament2, calcul_incasari(lista, medicament2));

	//ex 4 
	int nr_retete_medic = 0;
	Reteta* retete_medic = copiere_reteta_medic(lista, "Dr. Alina Ionescu", &nr_retete_medic);
	printf("Retetele pentru medicul Dr. Alina Ionescu sunt: %d\n", nr_retete_medic);
	
	//ex5
	float total_compensat = valoare_compensata(retete_medic, nr_retete_medic);
	printf("Valoarea totala compensata pentru medicul Dr. Alina Ionescu este: %.2f\n", total_compensat);
	
	//ex6
	char medicament3[] = "Paracetamol";
	unsigned int cantitate = cantitate_vanduta(lista, medicament3);
	printf("Cantitatea vanduta pentru medicamentul %s este: %u\n", medicament3, cantitate);
	
	//ex 7
	dezalocare(&lista);
	
	
	return 0;

}