<?php

use App\Models\Komentar;
use Illuminate\Database\Seeder;

class KomentarSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $komentare = [
            [
                'je_zamestnanec' => True,
                'komentar' => 'Je to cely rozbity sefe :(',
                'problem_id' => 1,
                'pouzivatel_id' => 3,
            ],
            [
                'je_zamestnanec' => False,
                'komentar' => 'hlboky vymol v strede cesty',
                'problem_id' => 1,
                'pouzivatel_id' => 1,
            ],
        ];

        foreach ($komentare as $komentar) {
            Komentar::create(array(
                'je_zamestnanec' => $komentar['je_zamestnanec'],
                'komentar' => $komentar['komentar'],
                'problem_id' => $komentar['problem_id'],
                'pouzivatel_id' => $komentar['pouzivatel_id']
            ));
        }
    }
}
