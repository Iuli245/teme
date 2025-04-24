using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace test_2023
{
    [Serializable]
    class FisaPacient : IContabil, ICloneable
    {
        private string Nume { get; set; }
        private string Simptome { get; set; }
        private int Durata_tratament { get; set; }
        private List<Medicament> Medicamente { get; set; }

        public FisaPacient(string n, string s, int d, List<Medicament> m)
        {
            Nume = n;
            Simptome = s;
            Durata_tratament = d;
            Medicamente = m;
        }
        public double CalculPretTratament()
        {
            return Medicamente.Sum(m => m.CalculPretTratament());
        }
        public object Clone()
        {
            return new FisaPacient(
                this.Nume,
                this.Simptome,
                this.Durata_tratament,
                new List<Medicament>(this.Medicamente.Select(m => new Medicament(m.Denumire, m.Pret, m.Cantitate)))
                );
        }

        public static FisaPacient operator +(FisaPacient f, Medicament m)
        {
            f.Medicamente.Add(m);
            return f;
        }

    }
}
