#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define DIM_HT 20

typedef struct Rezervare {
    unsigned int id;
    char* denumire_hotel;
    unsigned char nr_camere_hotel;
    unsigned char nr_camere_rezervate;
    char* nume_client;
    char* perioada;
    float pret_camera;
} Rezervare;

// Tabela de dispersie cu linear probing
Rezervare* tabela[DIM_HT];

// functie hash
int hash(char* denumire) {
    int s = 0;
    for (int i = 0; denumire[i]; i++)
        s += denumire[i];
    return s % DIM_HT;
}

// Cerinta 2: inserare rezervare in tabela de dispersie
void inserare_HT(Rezervare* r) {
    int poz = hash(r->denumire_hotel);
    while (tabela[poz] != NULL)
        poz = (poz + 1) % DIM_HT;
    tabela[poz] = r;
}

// Citire din fisier
void citireRezervari(const char* numeFisier) {
    FILE* f = fopen(numeFisier, "r");
    char buffer[128];

    while (fgets(buffer, sizeof(buffer), f)) {
        Rezervare* r = malloc(sizeof(Rezervare));
        r->id = atoi(buffer);

        fgets(buffer, sizeof(buffer), f);
        buffer[strcspn(buffer, "\n")] = 0;
        r->denumire_hotel = _strdup(buffer);

        fgets(buffer, sizeof(buffer), f);
        r->nr_camere_hotel = atoi(buffer);

        fgets(buffer, sizeof(buffer), f);
        r->nr_camere_rezervate = atoi(buffer);

        fgets(buffer, sizeof(buffer), f);
        buffer[strcspn(buffer, "\n")] = 0;
        r->nume_client = _strdup(buffer);

        fgets(buffer, sizeof(buffer), f);
        buffer[strcspn(buffer, "\n")] = 0;
        r->perioada = _strdup(buffer);

        fgets(buffer, sizeof(buffer), f);
        r->pret_camera = atof(buffer);

        inserare_HT(r);
    }
    fclose(f);
}

// Cerinta 2: Afisare tabela
void afisareTabela() {
    for (int i = 0; i < DIM_HT; i++) {
        if (tabela[i])
            printf("[%d]: %s - %s (%d camere rezervate din %d)\n", i,
                tabela[i]->denumire_hotel, tabela[i]->nume_client,
                tabela[i]->nr_camere_rezervate, tabela[i]->nr_camere_hotel);
        else
            printf("[%d]: --\n", i);
    }
}

// Cerinta 3: Grad de ocupare hotel
float gradOcupare(const char* hotel) {
    for (int i = 0; i < DIM_HT; i++) {
        if (tabela[i] && strcmp(tabela[i]->denumire_hotel, hotel) == 0) {
            return (float)tabela[i]->nr_camere_rezervate / tabela[i]->nr_camere_hotel * 100;
        }
    }
    return 0;
}

// Cerinta 4: Copiere rezervari client in vector
Rezervare** rezervariClient(const char* hotel, const char* client, int* nr_rezervari) {
    Rezervare** v = NULL;
    *nr_rezervari = 0;
    for (int i = 0; i < DIM_HT; i++) {
        if (tabela[i] && strcmp(tabela[i]->denumire_hotel, hotel) == 0 && strcmp(tabela[i]->nume_client, client) == 0) {
            v = realloc(v, (++(*nr_rezervari)) * sizeof(Rezervare*));
            v[*nr_rezervari - 1] = tabela[i];
        }
    }
    return v;
}

// Cerinta 5: Calcul incasari rezervari
float calculIncasari(Rezervare** rezervari, int nr, const char* perioada) {
    float incasari = 0;
    for (int i = 0; i < nr; i++) {
        if (strcmp(rezervari[i]->perioada, perioada) == 0)
            incasari += rezervari[i]->nr_camere_rezervate * rezervari[i]->pret_camera;
    }
    return incasari;
}

// Cerinta 6: Numar camere rezervate de un client
int camereRezervateClient(const char* client) {
    int total = 0;
    for (int i = 0; i < DIM_HT; i++) {
        if (tabela[i] && strcmp(tabela[i]->nume_client, client) == 0)
            total += tabela[i]->nr_camere_rezervate;
    }
    return total;
}

// Cerinta 7: Dezalocare memorie
void dezalocare() {
    for (int i = 0; i < DIM_HT; i++) {
        if (tabela[i]) {
            free(tabela[i]->denumire_hotel);
            free(tabela[i]->nume_client);
            free(tabela[i]->perioada);
            free(tabela[i]);
        }
    }
}

int main() {
    for (int i = 0; i < DIM_HT; i++) tabela[i] = NULL;

    citireRezervari("Rezervari.txt");
    afisareTabela();

    printf("\nGrad ocupare Hotel Continental: %.2f%%\n", gradOcupare("Hotel Continental"));

    int nr_rezervari = 0;
    Rezervare** rezClient = rezervariClient("Hotel Continental", "Marinescu Ana", &nr_rezervari);
    printf("\nRezervari pentru Marinescu Ana la Hotel Continental: %d\n", nr_rezervari);

    float incasari = calculIncasari(rezClient, nr_rezervari, "2024-09-01/2024-09-05");
    printf("Incasari Marinescu Ana perioada specificata: %.2f\n", incasari);

    printf("\nCamere rezervate de Ionescu Mihai: %d\n", camereRezervateClient("Ionescu Mihai"));

    dezalocare();
    free(rezClient);
    return 0;
}
