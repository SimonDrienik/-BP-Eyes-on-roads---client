<?php

use App\Models\Problem;
use Illuminate\Database\Seeder;

class ProblemSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $problemy = [
            [
                'poloha' => '45.333, 30.888',
                'popis_problemu' => 'vymol jak hovado',
                'priorita_id' => '1',
                'cesta_id' => '1',
                'pouzivatel_id' => '1',
                'kategoria_problemu_id' => '1',
                'stav_problemu_id' => '1',
            ]
        ];

        foreach ($problemy as $problem) {
            Problem::create(array(
                'poloha' => $problem['poloha'],
                'popis_problemu' => $problem['popis_problemu'],
                'priorita_id' => $problem['priorita_id'],
                'cesta_id' => $problem['cesta_id'],
                'pouzivatel_id' => $problem['pouzivatel_id'],
                'kategoria_problemu_id' => $problem['kategoria_problemu_id'],
                'stav_problemu_id' => $problem['stav_problemu_id'],
            ));
        }
    }
}
