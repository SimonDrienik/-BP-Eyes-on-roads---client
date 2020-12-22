<?php

use App\Models\PriradenyZamestnanec;
use Illuminate\Database\Seeder;

class PriradenyZamestnanecSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $priradene = [
            [
                'problem_id' => 1,
                'zamestnanec_id' => 3,
            ],

        ];

        foreach ($priradene as $priradeny_zamestnanec) {
            PriradenyZamestnanec::create(array(
                'problem_id' => $priradeny_zamestnanec['problem_id'],
                'zamestnanec_id' => $priradeny_zamestnanec['zamestnanec_id']
            ));
        }
    }
}
