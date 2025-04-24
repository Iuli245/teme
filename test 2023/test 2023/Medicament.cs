using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace test_2023
{

    [Serializable]
   public class Medicament : IContabil
    {
        private const int Cod = 1;
        private string Denumire {  get; set; }
        private float Pret {  get; set; }
        private float Cantitate {  get; set; }

        public Medicament( string d, float p, float c)
        {
        
            Denumire = d;
            Pret = p;
            Cantitate = c;
        }
       

        public double CalculPretTratament()
        {
            return Pret * Cantitate;
        }

        public override string ToString()
        {
            return "Medicamentul " + Denumire + " are codul " + Cod + " cu pretul " + Pret + " si cantitate de " + Cantitate;

        }
      
    }
}
